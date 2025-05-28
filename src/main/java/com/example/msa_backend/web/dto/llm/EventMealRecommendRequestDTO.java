package com.example.msa_backend.web.dto.llm;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

public class EventMealRecommendRequestDTO {

    @Getter
    @RequiredArgsConstructor
    public static class Request {

        private final String date;
        private final String mealType;
        private final int count;
        private final String weather;
        private final int temperature;
        private final List<String> events;
    }
}