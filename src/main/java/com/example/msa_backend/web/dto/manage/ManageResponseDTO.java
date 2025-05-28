package com.example.msa_backend.web.dto.manage;

import com.example.msa_backend.domain.AtePeople;
import com.example.msa_backend.domain.enums.MealType;
import com.example.msa_backend.domain.enums.Weather;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
public class ManageResponseDTO {

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class ManagePeopleDTO {
        private final LocalDate date;
        private final Long people;
        private final MealType mealType;

        public static ManagePeopleDTO toDTO(LocalDate date, Long people, MealType mealType) {
            return ManagePeopleDTO.builder()
                    .date(date)
                    .people(people)
                    .mealType(mealType)
                    .build();
        }
    }
}
