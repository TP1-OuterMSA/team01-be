package com.example.msa_backend.service.atepeople;

import com.example.msa_backend.web.dto.people.AtePeopleRequestDTO;
import com.example.msa_backend.web.dto.people.AtePeopleResponseDTO;

import java.time.LocalDate;

public interface AtePeopleService {

    AtePeopleResponseDTO.AtePeopleDTO postAtePeople(AtePeopleRequestDTO.addDTO atePeopleRequestDTO);

    AtePeopleResponseDTO.PredictPeople getPredictPeople(LocalDate date, String mealType);

    AtePeopleResponseDTO.ComparePeople getComparePeople(LocalDate date, String mealType);

}
