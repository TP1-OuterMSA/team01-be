package com.example.msa_backend.service.manage;


import com.example.msa_backend.web.dto.manage.ManageResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface ManageService {
    List<ManageResponseDTO.ManagePeopleDTO> getRecommendedStaffByDate(LocalDate date);
}
