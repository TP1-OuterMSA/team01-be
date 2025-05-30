package com.example.msa_backend.web.controller;

import com.example.msa_backend.service.atepeople.AtePeopleService;
import com.example.msa_backend.web.dto.people.AtePeopleRequestDTO;
import com.example.msa_backend.web.dto.people.AtePeopleResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


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
    public AtePeopleResponseDTO.PredictPeopleWithExplanation getPredict(
            @RequestParam LocalDate date,
            @RequestParam String mealType
    ) {
        return atePeopleService.getPredictPeople(date, mealType);
    }

    @GetMapping("/compare")
    public AtePeopleResponseDTO.ComparePeople getComparePeople(
            @RequestParam LocalDate date,
            @RequestParam String mealType
    ) {
        return atePeopleService.getComparePeople(date, mealType);
    }
}
