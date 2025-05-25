package com.example.msa_backend.service.atepeople;

import com.example.msa_backend.converter.AtePeopleConverter;
import com.example.msa_backend.converter.WeatherConverter;
import com.example.msa_backend.domain.AtePeople;
import com.example.msa_backend.domain.WeatherLog;
import com.example.msa_backend.domain.enums.MealType;
import com.example.msa_backend.domain.enums.Weather;
import com.example.msa_backend.repository.AtePeopleRepository;
import com.example.msa_backend.repository.WeatherRepository;
import com.example.msa_backend.web.dto.people.AtePeopleRequestDTO;
import com.example.msa_backend.web.dto.people.AtePeopleResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AtePeopleServiceImpl implements AtePeopleService {

    private final AtePeopleRepository atePeopleRepository;
    private final WeatherRepository weatherRepository;

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
    public AtePeopleResponseDTO.PredictPeople getPredictPeople(LocalDate date, String mealType) {
        LocalDate toDate = date.minusDays(1);       // 전날
        LocalDate fromDate = toDate.minusDays(6);   // 1주일 전

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

        double avg = filtered.stream()
                .mapToLong(AtePeople::getPeople)
                .average()
                .orElse(0.0);
        Long roundedAvg = Math.round(avg);

        // 🔍 예측 대상 날짜 + mealType → time 계산
        LocalTime time = null;
        if (finalMealType != null) {
            switch (finalMealType) {
                case BREAKFAST -> time = LocalTime.of(9, 0);
                case LUNCH     -> time = LocalTime.of(12, 0);
                case DINNER    -> time = LocalTime.of(18, 0);
            }
        }

        WeatherLog loggedWeather = weatherRepository.findTopByDateAndTime(date, time);
        String rawStatus = (loggedWeather != null) ? loggedWeather.getStatus() : null;
        Weather weatherEnum = WeatherConverter.fromStatus(rawStatus);

        return AtePeopleResponseDTO.PredictPeople.toPredictDTO(
                AtePeople.builder()
                        .date(date)
                        .time(time)
                        .mealType(finalMealType)
                        .people(roundedAvg)
                        .build(),
                weatherEnum
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

        // 예측값 계산
        AtePeopleResponseDTO.PredictPeople predicted = getPredictPeople(date, mealType);

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
                weatherEnum
        );
    }
}
