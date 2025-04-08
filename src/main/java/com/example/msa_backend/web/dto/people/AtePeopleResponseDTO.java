package com.example.msa_backend.web.dto.people;

import com.example.msa_backend.domain.AtePeople;
import com.example.msa_backend.domain.FoodWaste;
import com.example.msa_backend.domain.enums.MealType;
import com.example.msa_backend.domain.enums.Weather;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
public class AtePeopleResponseDTO {

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class AtePeopleDTO {
        private final Long id;
        private final LocalDate date;
        private final Long people;
        private final MealType mealType;
        private final Weather weather;

        public static AtePeopleDTO toDTO(AtePeople people) {
            return AtePeopleDTO.builder()
                    .id(people.getId())
                    .date(people.getDate())
                    .people(people.getPeople())
                    .mealType(people.getMealType())
                    .weather(people.getWeather())
                    .build();
        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class PredictPeople {
        private final Long id;
        private final LocalDate date;
        private final Long people;
        private final MealType mealType;
        private final Weather weather;

        public static AtePeopleDTO toPredictDTO(AtePeople people) {
            return AtePeopleDTO.builder()
                    .id(people.getId())
                    .date(people.getDate())
                    .people(people.getPeople())
                    .mealType(people.getMealType())
                    .weather(people.getWeather())
                    .build();
        }
    }
}
