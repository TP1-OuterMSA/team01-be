package com.example.msa_backend.web.dto.payment;

import com.example.msa_backend.domain.FoodWaste;
import com.example.msa_backend.domain.PaymentLog;
import com.example.msa_backend.domain.enums.MealType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
public class PaymentResponseDTO {

    @Getter
    @Builder
    public static class PaymentDTO {
        private final LocalDate date;
        private final List<MealTypeInfo> mealTypeInfos;

        @Getter
        @Builder
        public static class MealTypeInfo {
            private final MealType mealType;
            private final Double amount;
        }

        public static PaymentDTO toDTO(LocalDate date, List<MealTypeInfo> infos) {
            return PaymentDTO.builder()
                    .date(date)
                    .mealTypeInfos(infos)
                    .build();
        }
    }
}

