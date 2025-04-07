package com.example.msa_backend.web.dto.people;

import com.example.msa_backend.domain.enums.MealType;
import com.example.msa_backend.domain.enums.Weather;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

public class AtePeopleRequestDTO {

    @Getter
    @RequiredArgsConstructor
    public static class addDTO {

        private final LocalDate date;
        private final MealType mealType;
        private final Long people;
        private final Weather weather;
    }
}
