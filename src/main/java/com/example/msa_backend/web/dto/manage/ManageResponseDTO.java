package com.example.msa_backend.web.dto.manage;

import com.example.msa_backend.domain.enums.MealType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

public class ManageResponseDTO {

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class ManagePeopleDTO {
        private final LocalDate date;
        private final Long people;
        private final MealType mealType;
        private final Long recommendedStaff;     // 운영 인원
        private final String recommendation;     // 운영 지침

        public static ManagePeopleDTO toDTO(
                LocalDate date, Long people, MealType mealType, Long recommendedStaff,String recommendation
        ) {
            return ManagePeopleDTO.builder()
                    .date(date)
                    .people(people)
                    .mealType(mealType)
                    .recommendedStaff(recommendedStaff)
                    .recommendation(recommendation)
                    .build();
        }
    }
}
