package com.example.msa_backend.converter;

import com.example.msa_backend.domain.AtePeople;
import com.example.msa_backend.domain.FoodWaste;
import com.example.msa_backend.web.dto.food.FoodWasteRequestDTO;
import com.example.msa_backend.web.dto.food.FoodWasteResponseDTO;
import com.example.msa_backend.web.dto.people.AtePeopleRequestDTO;
import com.example.msa_backend.web.dto.people.AtePeopleResponseDTO;

public class AtePeopleConverter {

    public static AtePeopleResponseDTO.AtePeopleDTO toAtePeopleResponseDTO(AtePeople atePeople) {
        return AtePeopleResponseDTO.AtePeopleDTO.toDTO(atePeople);
    }

    public static AtePeople toAtePeople(AtePeopleRequestDTO.addDTO atePeopleDTO) {
        return AtePeople.builder()
                .date(atePeopleDTO.getDate())
                .people(atePeopleDTO.getPeople())
                .mealType(atePeopleDTO.getMealType())
                .weather((atePeopleDTO.getWeather()))
                .build();
    }
}
