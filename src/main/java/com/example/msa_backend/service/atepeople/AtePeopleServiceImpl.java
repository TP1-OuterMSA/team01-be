package com.example.msa_backend.service.atepeople;

import com.example.msa_backend.converter.AtePeopleConverter;
import com.example.msa_backend.converter.WeatherConverter;
import com.example.msa_backend.domain.AtePeople;
import com.example.msa_backend.domain.Event;
import com.example.msa_backend.domain.WeatherLog;
import com.example.msa_backend.domain.enums.MealType;
import com.example.msa_backend.domain.enums.Weather;
import com.example.msa_backend.domain.ml.LinearRegressionModel;
import com.example.msa_backend.repository.AtePeopleRepository;
import com.example.msa_backend.repository.EventRepository;
import com.example.msa_backend.repository.WeatherRepository;
import com.example.msa_backend.web.dto.people.AtePeopleRequestDTO;
import com.example.msa_backend.web.dto.people.AtePeopleResponseDTO;
import com.example.msa_backend.domain.ml.DataPoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AtePeopleServiceImpl implements AtePeopleService {

    private final AtePeopleRepository atePeopleRepository;
    private final WeatherRepository weatherRepository;
    private final EventRepository eventRepository;

    @Override
    public AtePeopleResponseDTO.AtePeopleDTO postAtePeople(AtePeopleRequestDTO.addDTO atePeopleRequestDTO) {
        LocalDate date = atePeopleRequestDTO.getDate();
        MealType mealType = atePeopleRequestDTO.getMealType();

        // 해당 날짜 + 식사 타입이 있는지 확인
        AtePeople existing = atePeopleRepository.findByDateAndMealType(date, mealType);

        if (existing != null) {
            // 기존 데이터가 있으면 값 업데이트
            existing.setPeople(atePeopleRequestDTO.getPeople());
            return AtePeopleConverter.toAtePeopleResponseDTO(existing);
        } else {
            // 없으면 새로 생성
            AtePeople atePeople = AtePeopleConverter.toAtePeople(atePeopleRequestDTO);
            return AtePeopleConverter.toAtePeopleResponseDTO(atePeopleRepository.save(atePeople));
        }
    }

    @Override
    public AtePeopleResponseDTO.PredictPeopleWithExplanation getPredictPeople(LocalDate date, String mealType) {
        LocalDate toDate = date.minusDays(1);       // 전날
        LocalDate fromDate = toDate.minusDays(13);  // 2주치

        List<AtePeople> filtered = atePeopleRepository.findAllByDateBetween(fromDate, toDate);

        MealType finalMealType = null;
        if (mealType != null && !mealType.isEmpty()) {
            try {
                finalMealType = MealType.valueOf(mealType.toUpperCase());
                MealType finalMealTypeForLambda = finalMealType;
                filtered = filtered.stream()
                        .filter(p -> p.getMealType() == finalMealTypeForLambda)
                        .collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid mealType: " + mealType);
            }
        }

        // 학습용 DataPoint 구성
        List<DataPoint> dataPoints = filtered.stream()
                .map(p -> {
                    int weekday = p.getDate().getDayOfWeek().getValue() % 7;
                    int eventParticipants = 0;
                    Event event = eventRepository.findByDate(p.getDate());
                    if (event != null && event.getPeople() != null) {
                        eventParticipants = (int) Math.round(event.getPeople());
                    }

                    WeatherLog weather = weatherRepository.findTopByDateAndTime(p.getDate(), p.getTime());
                    double temperature = (weather != null) ? weather.getTemperature() : 20.0;
                    double humidity = (weather != null) ? weather.getHumidity() : 50.0;
                    int weatherCode = WeatherConverter.toCode(weather != null ? weather.getStatus() : null);

                    return new DataPoint(weekday, eventParticipants, temperature, humidity, weatherCode, Math.toIntExact(p.getPeople()));
                })
                .toList();

        // 회귀 모델 학습
        LinearRegressionModel model = new LinearRegressionModel();
        boolean useRegression = dataPoints.size() >= 6;
        if (useRegression) {
            model.train(dataPoints);
        }

        // 예측용 입력 구성
        int weekday = date.getDayOfWeek().getValue() % 7;
        int todayEventPeople = 0;
        Event todayEvent = eventRepository.findByDate(date);
        if (todayEvent != null && todayEvent.getPeople() != null) {
            todayEventPeople = (int) Math.round(todayEvent.getPeople());
        }

        LocalTime time = null;
        if (finalMealType != null) {
            switch (finalMealType) {
                case BREAKFAST -> time = LocalTime.of(9, 0);
                case LUNCH     -> time = LocalTime.of(12, 0);
                case DINNER    -> time = LocalTime.of(18, 0);
            }
        }

        WeatherLog loggedWeather = weatherRepository.findTopByDateAndTime(date, time);
        double temperature = (loggedWeather != null) ? loggedWeather.getTemperature() : 20.0;
        double humidity = (loggedWeather != null) ? loggedWeather.getHumidity() : 50.0;
        int weatherCode = WeatherConverter.toCode(loggedWeather != null ? loggedWeather.getStatus() : null);

        int predictedPeople;
        String explanation;

        if (useRegression) {
            predictedPeople = model.predict(weekday, todayEventPeople, temperature, humidity, weatherCode);
            explanation = model.explainPrediction(weekday, todayEventPeople, temperature, humidity, weatherCode);
        } else {
            predictedPeople = Math.toIntExact(Math.round(filtered.stream().mapToLong(AtePeople::getPeople).average().orElse(0)));
            explanation = "충분한 데이터가 없어 단순 평균으로 예측되었습니다.";
        }

        Weather weatherEnum = WeatherConverter.fromStatus(loggedWeather != null ? loggedWeather.getStatus() : null);

        return AtePeopleResponseDTO.PredictPeopleWithExplanation.of(
                AtePeople.builder()
                        .date(date)
                        .time(time)
                        .mealType(finalMealType)
                        .people((long) predictedPeople)
                        .build(),
                weatherEnum,
                explanation
        );
    }


    @Override
    public AtePeopleResponseDTO.ComparePeople getComparePeople(LocalDate date, String mealType) {
        MealType finalMealType;
        try {
            finalMealType = MealType.valueOf(mealType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid mealType: " + mealType);
        }

        AtePeople actual = atePeopleRepository.findByDateAndMealType(date, finalMealType);
        if (actual == null) {
            throw new IllegalArgumentException("No actual record found for given date and mealType.");
        }

        // 예측 시간 계산
        LocalTime time = switch (finalMealType) {
            case BREAKFAST -> LocalTime.of(9, 0);
            case LUNCH     -> LocalTime.of(12, 0);
            case DINNER    -> LocalTime.of(18, 0);
        };

        WeatherLog weatherLog = weatherRepository.findTopByDateAndTime(date, time);
        Weather weatherEnum = WeatherConverter.fromStatus(
                weatherLog != null ? weatherLog.getStatus() : null
        );

        // 예측값 계산 + 설명
        AtePeopleResponseDTO.PredictPeopleWithExplanation predicted = getPredictPeople(date, mealType);

        // 예측용 AtePeople 가짜 객체 생성
        AtePeople predictedAsEntity = AtePeople.builder()
                .date(date)
                .mealType(finalMealType)
                .people(predicted.getPeople())
                .build();

        // 응답 생성
        return AtePeopleResponseDTO.ComparePeople.toComparePeople(
                actual,
                predictedAsEntity,
                weatherEnum,
                predicted.getExplanation() // 설명 추가
        );
    }

}
