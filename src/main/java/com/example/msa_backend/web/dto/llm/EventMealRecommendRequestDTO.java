package com.example.msa_backend.web.dto.llm;

import com.example.msa_backend.domain.enums.MealType;
import com.example.msa_backend.domain.enums.Weather;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class EventMealRecommendRequestDTO {

    @Getter
    @RequiredArgsConstructor
    public static class Request {

        private final LocalDate date;
        private final MealType mealType;
        private final Long count;
        private final Weather weather;
        private final double temperature;
        private final List<String> events;
    }
}