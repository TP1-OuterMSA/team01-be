package com.example.msa_backend.web.dto.food;

import com.example.msa_backend.domain.enums.MealType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

public class FoodWasteRequestDTO {

    @Getter
    @RequiredArgsConstructor
    public static class addDTO {

        private final LocalDate date;
        private final Double amount;
        private final MealType mealType;
    }
}
