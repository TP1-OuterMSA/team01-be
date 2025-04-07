package com.example.msa_backend.converter;

import com.example.msa_backend.domain.FoodWaste;
import com.example.msa_backend.web.dto.food.FoodWasteRequestDTO;
import com.example.msa_backend.web.dto.food.FoodWasteResponseDTO;

public class FoodWasteConverter {

    public static FoodWasteResponseDTO.FoodWasteDTO toWasteResponseDTO(FoodWaste foodWaste) {
        return FoodWasteResponseDTO.FoodWasteDTO.toDTO(foodWaste);
    }

    public static FoodWaste toFoodWaste(FoodWasteRequestDTO.addDTO foodWasteDTO) {
        return FoodWaste.builder()
                .date(foodWasteDTO.getDate())
                .amount(foodWasteDTO.getAmount())
                .mealType(foodWasteDTO.getMealType())
                .build();
    }
}
