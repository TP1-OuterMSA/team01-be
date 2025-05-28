package com.example.msa_backend.web.controller;

import com.example.msa_backend.service.manage.ManageService;
import com.example.msa_backend.web.dto.manage.ManageResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/manage")
@RequiredArgsConstructor
public class ManageController {
    private final ManageService manageService;

    @GetMapping("/people")
    public List<ManageResponseDTO.ManagePeopleDTO> getManagePeople (
            @RequestParam LocalDate date
    ) {
        return manageService.getRecommendedStaffByDate(date);
    }
}