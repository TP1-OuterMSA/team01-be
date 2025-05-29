package com.example.msa_backend.web.dto.llm;

import com.example.msa_backend.domain.enums.MealType;
import com.example.msa_backend.web.dto.manage.ManageResponseDTO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

public class EventMealRecommendResponseDTO {


    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class RecommendResponseDTO {
        private final LocalDate date;
        private final Long people;
        private final MealType mealType;

        public static EventMealRecommendResponseDTO.RecommendResponseDTO toDTO(LocalDate date, Long people, MealType mealType) {
            return EventMealRecommendResponseDTO.RecommendResponseDTO.builder()
                    .date(date)
                    .people(people)
                    .mealType(mealType)
                    .build();
        }
    }
}
