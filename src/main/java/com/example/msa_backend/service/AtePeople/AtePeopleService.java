package com.example.msa_backend.service.AtePeople;

import com.example.msa_backend.web.dto.food.FoodWasteRequestDTO;
import com.example.msa_backend.web.dto.food.FoodWasteResponseDTO;
import com.example.msa_backend.web.dto.people.AtePeopleRequestDTO;
import com.example.msa_backend.web.dto.people.AtePeopleResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface AtePeopleService {

    AtePeopleResponseDTO.AtePeopleDTO postAtePeople(AtePeopleRequestDTO.addDTO atePeopleRequestDTO);

    AtePeopleResponseDTO.AtePeopleDTO getPredictPeople(LocalDate date, String mealType, String weather);
}
