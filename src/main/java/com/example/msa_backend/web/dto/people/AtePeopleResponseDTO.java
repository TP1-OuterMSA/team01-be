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
        private final LocalDate date;
        private final Long people;
        private final MealType mealType;

        public static AtePeopleDTO toDTO(AtePeople people) {
            return AtePeopleDTO.builder()
                    .date(people.getDate())
                    .people(people.getPeople())
                    .mealType(people.getMealType())
                    .build();
        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class PredictPeople {
        private final LocalDate date;
        private final Long people;
        private final MealType mealType;
        private final Weather weatherStatus; // ✅ enum 기반 필드

        public static PredictPeople toPredictDTO(AtePeople people, Weather weatherStatus) {
            return PredictPeople.builder()
                    .date(people.getDate())
                    .people(people.getPeople())
                    .mealType(people.getMealType())
                    .weatherStatus(weatherStatus) // ✅ 전달
                    .build();
        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class ComparePeople {
        private final LocalDate date;
        private final Long atePeople;
        private final Long predictPeople;
        private final MealType mealType;
        private final Weather weatherStatus; // ✅ enum 기반 필드

        public static ComparePeople toComparePeople(AtePeople atePeople, AtePeople predictPeople, Weather weatherStatus) {
            return ComparePeople.builder()
                    .date(atePeople.getDate())
                    .atePeople(atePeople.getPeople())
                    .predictPeople(predictPeople.getPeople())
                    .mealType(atePeople.getMealType())
                    .weatherStatus(weatherStatus) // ✅ 전달
                    .build();
        }
    }
}
