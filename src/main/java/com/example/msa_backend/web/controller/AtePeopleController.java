package com.example.msa_backend.web.controller;


import com.example.msa_backend.service.AtePeople.AtePeopleService;
import com.example.msa_backend.service.FoodWaste.FoodWasteService;
import com.example.msa_backend.web.dto.food.FoodWasteRequestDTO;
import com.example.msa_backend.web.dto.food.FoodWasteResponseDTO;
import com.example.msa_backend.web.dto.people.AtePeopleRequestDTO;
import com.example.msa_backend.web.dto.people.AtePeopleResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/people")
@RequiredArgsConstructor
public class AtePeopleController {
    private final AtePeopleService atePeopleService;

    @PutMapping()
    public AtePeopleResponseDTO.AtePeopleDTO postAtePeople (
            @RequestBody AtePeopleRequestDTO.addDTO atePeopleDTO
    ) {
        return atePeopleService.postAtePeople(atePeopleDTO);
    }

    @GetMapping("/predict")
    public AtePeopleResponseDTO.AtePeopleDTO getWaste(
            @RequestParam LocalDate date,
            @RequestParam String mealType,
            @RequestParam String weather
    ) {
        return atePeopleService.getPredictPeople(date, mealType, weather);
    }
}
