package com.example.msa_backend.service.foodwaste;

import com.example.msa_backend.web.dto.food.FoodWasteRequestDTO;
import com.example.msa_backend.web.dto.food.FoodWasteResponseDTO;

import java.util.List;

public interface FoodWasteService {

    FoodWasteResponseDTO.FoodWasteDTO postFoodWaste(FoodWasteRequestDTO.addDTO foodWasteRequestDTO);

    List<FoodWasteResponseDTO.FoodWasteDTO> getFoodWastes(String periods, String mealType);

}
