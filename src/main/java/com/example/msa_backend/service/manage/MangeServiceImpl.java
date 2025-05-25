package com.example.msa_backend.service.manage;

import com.example.msa_backend.domain.AtePeople;
import com.example.msa_backend.domain.enums.MealType;
import com.example.msa_backend.repository.AtePeopleRepository;
import com.example.msa_backend.service.atepeople.AtePeopleService;
import com.example.msa_backend.web.dto.manage.ManageResponseDTO;
import com.example.msa_backend.web.dto.people.AtePeopleResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MangeServiceImpl implements ManageService {

    private final AtePeopleService atePeopleService;

    @Override
    public List<ManageResponseDTO.ManagePeopleDTO> getRecommendedStaffByDate(LocalDate date) {
        List<ManageResponseDTO.ManagePeopleDTO> result = new ArrayList<>();

        for (MealType mealType : MealType.values()) {
            try {
                // 1. 예측 인원 조회 (문자열 파라미터로 mealType 넘김)
                AtePeopleResponseDTO.PredictPeople predicted = atePeopleService.getPredictPeople(date, mealType.name());

                if (predicted != null && predicted.getPeople() != null) {
                    long predictedPeople = predicted.getPeople();
                    long staffCount = calculateRecommendedStaff(predictedPeople);

                    result.add(ManageResponseDTO.ManagePeopleDTO.toDTO(date, staffCount, mealType));
                }
            } catch (Exception e) {
                // mealType이 잘못되거나 예측 실패할 경우 스킵
                continue;
            }
        }

        return result;
    }

    private long calculateRecommendedStaff(long people) {
        if (people <= 100) return 1;
        else if (people <= 300) return 2;
        else if (people <= 500) return 3;
        else if (people <= 700) return 4;
        else if (people <= 900) return 5;
        else return 6;
    }
}
