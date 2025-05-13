package com.example.msa_backend.web.controller;


import com.example.msa_backend.service.foodwaste.FoodWasteService;
import com.example.msa_backend.web.dto.food.FoodWasteRequestDTO;
import com.example.msa_backend.web.dto.food.FoodWasteResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/waste")
@RequiredArgsConstructor
public class FoodWasteController {
    private final FoodWasteService foodWasteService;

    @PutMapping()
    public FoodWasteResponseDTO.FoodWasteDTO postWaste (
            @RequestBody FoodWasteRequestDTO.addDTO foodWasteDTO
    ) {
        return foodWasteService.postFoodWaste(foodWasteDTO);
    }

    @GetMapping("/recent")
    public List<FoodWasteResponseDTO.FoodWasteDTO> getWaste(
            @RequestParam String periods,
            @RequestParam String mealType
    ) {
        return foodWasteService.getFoodWastes(periods, mealType);
    }
}
