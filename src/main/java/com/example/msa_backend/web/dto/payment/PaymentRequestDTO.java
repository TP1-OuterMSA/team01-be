package com.example.msa_backend.web.dto.payment;

import com.example.msa_backend.domain.enums.MealType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;

public class PaymentRequestDTO {

    @Getter
    @RequiredArgsConstructor
    public static class addDTO {

        private final ArrayList<mealTypeDTO> infos;
        private final LocalDate date;
    }

    @Getter
    @RequiredArgsConstructor
    public static class mealTypeDTO {
        private final MealType mealType;
        private final Double amount;
    }
}
