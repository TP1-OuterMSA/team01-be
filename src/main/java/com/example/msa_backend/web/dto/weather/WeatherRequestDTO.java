package com.example.msa_backend.web.dto.weather;

import com.example.msa_backend.domain.enums.MealType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

public class WeatherRequestDTO {

    @Getter
    @RequiredArgsConstructor
    public static class addDTO {
        private final LocalDate date;
        private final MealType mealType;
        private final Long people;
    }
}
