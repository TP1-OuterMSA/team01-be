package com.example.msa_backend.web.dto.food;

import com.example.msa_backend.domain.FoodWaste;
import com.example.msa_backend.domain.enums.MealType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
public class FoodWasteResponseDTO {

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class FoodWasteDTO {
        private final Long id;
        private final LocalDate date;
        private final Double amount;
        private final MealType mealType;

        public static FoodWasteDTO toDTO(FoodWaste waste) {
            return FoodWasteDTO.builder()
                    .id(waste.getId())
                    .date(waste.getDate())
                    .amount(waste.getAmount())
                    .mealType(waste.getMealType())
                    .build();
        }
    }
}
