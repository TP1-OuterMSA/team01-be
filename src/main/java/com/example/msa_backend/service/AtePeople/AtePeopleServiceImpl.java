package com.example.msa_backend.service.AtePeople;

import com.example.msa_backend.converter.AtePeopleConverter;
import com.example.msa_backend.converter.FoodWasteConverter;
import com.example.msa_backend.domain.AtePeople;
import com.example.msa_backend.domain.FoodWaste;
import com.example.msa_backend.domain.enums.MealType;
import com.example.msa_backend.domain.enums.Weather;
import com.example.msa_backend.repository.AtePeopleRepository;
import com.example.msa_backend.repository.FoodWasteRepository;
import com.example.msa_backend.web.dto.food.FoodWasteRequestDTO;
import com.example.msa_backend.web.dto.food.FoodWasteResponseDTO;
import com.example.msa_backend.web.dto.people.AtePeopleRequestDTO;
import com.example.msa_backend.web.dto.people.AtePeopleResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AtePeopleServiceImpl implements AtePeopleService {

    private final AtePeopleRepository atePeopleRepository;


    @Override
    public AtePeopleResponseDTO.AtePeopleDTO postAtePeople(AtePeopleRequestDTO.addDTO atePeopleRequestDTO) {
        LocalDate date = atePeopleRequestDTO.getDate();
        MealType mealType = atePeopleRequestDTO.getMealType();

        // 해당 날짜 + 식사 타입이 있는지 확인
        AtePeople existing = atePeopleRepository.findByDateAndMealType(date, mealType);

        if (existing != null) {
            // 기존 데이터가 있으면 값 업데이트
            existing.setPeople(atePeopleRequestDTO.getPeople());
            existing.setWeather(atePeopleRequestDTO.getWeather());
            return AtePeopleConverter.toAtePeopleResponseDTO(existing);
        } else {
            // 없으면 새로 생성
            AtePeople atePeople = AtePeopleConverter.toAtePeople(atePeopleRequestDTO);
            return AtePeopleConverter.toAtePeopleResponseDTO(atePeopleRepository.save(atePeople));
        }
    }


    @Override
    public AtePeopleResponseDTO.AtePeopleDTO getPredictPeople(LocalDate date, String mealType, String weather) {
        LocalDate toDate = date.minusDays(1);       // 전날
        LocalDate fromDate = toDate.minusDays(6);   // 1주일 준

        List<AtePeople> filtered = atePeopleRepository.findAllByDateBetween(fromDate, toDate);

        if (mealType != null && !mealType.isEmpty()) {
            try {
                MealType finalMealType = MealType.valueOf(mealType.toUpperCase());
                filtered = filtered.stream()
                        .filter(p -> p.getMealType() == finalMealType)
                        .collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid mealType: " + mealType);
            }
        }

        if (weather != null && !weather.isEmpty()) {
            try {
                Weather finalWeather = Weather.valueOf(weather.toUpperCase());
                filtered = filtered.stream()
                        .filter(p -> p.getWeather() == finalWeather)
                        .collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid weather: " + weather);
            }
        }

        double avg = filtered.stream()
                .mapToLong(AtePeople::getPeople)
                .average()
                .orElse(0.0);

        Long roundedAvg = Math.round(avg);

        MealType finalMealType = mealType != null ? MealType.valueOf(mealType.toUpperCase()) : null;
        Weather finalWeather = weather != null ? Weather.valueOf(weather.toUpperCase()) : null;

        return AtePeopleResponseDTO.AtePeopleDTO.toDTO(
                AtePeople.builder()
                        .date(date)
                        .mealType(finalMealType)
                        .weather(finalWeather)
                        .people(roundedAvg)
                        .build()
        );
    }
}
