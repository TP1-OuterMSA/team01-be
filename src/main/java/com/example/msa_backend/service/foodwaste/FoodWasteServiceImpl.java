package com.example.msa_backend.service.foodwaste;

import com.example.msa_backend.converter.FoodWasteConverter;
import com.example.msa_backend.domain.FoodWaste;
import com.example.msa_backend.domain.enums.MealType;
import com.example.msa_backend.repository.FoodWasteRepository;
import com.example.msa_backend.web.dto.food.FoodWasteRequestDTO;
import com.example.msa_backend.web.dto.food.FoodWasteResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class FoodWasteServiceImpl implements FoodWasteService {

    private final FoodWasteRepository foodWasteRepository;


    @Override
    public FoodWasteResponseDTO.FoodWasteDTO postFoodWaste(FoodWasteRequestDTO.addDTO foodWasteRequestDTO) {
        LocalDate date = foodWasteRequestDTO.getDate();
        MealType mealType = foodWasteRequestDTO.getMealType();

        // 해당 날짜 + 식사 타입이 있는지 확인
        FoodWaste existing = foodWasteRepository.findByDateAndMealType(date, mealType);

        if (existing != null) {
            // 기존 데이터가 있으면 값 업데이트
            existing.setAmount(foodWasteRequestDTO.getAmount());
            return FoodWasteConverter.toWasteResponseDTO(existing);
        } else {
            // 없으면 새로 생성
            FoodWaste foodWaste = FoodWasteConverter.toFoodWaste(foodWasteRequestDTO);
            return FoodWasteConverter.toWasteResponseDTO(foodWasteRepository.save(foodWaste));
        }
    }


    @Override
    public List<FoodWasteResponseDTO.FoodWasteDTO> getFoodWastes(String periods, String mealType) {
        LocalDate today = LocalDate.now();

        LocalDate fromDate = switch (periods.toLowerCase()) {
            case "week" -> today.minusDays(6);     // 오늘 포함 7일
            case "month" -> today.minusDays(29);   // 오늘 포함 30일
            case "quarter" -> today.minusDays(89); // 오늘 포함 90일
            default -> throw new IllegalArgumentException("Invalid period: " + periods);
        };

        // 날짜 범위 조회
        List<FoodWaste> filtered = foodWasteRepository.findAllByDateBetween(fromDate, today);

        // mealType 필터 적용
        if (mealType != null && !mealType.isEmpty()) {
            try {
                MealType filterType = MealType.valueOf(mealType.toUpperCase());
                filtered = filtered.stream()
                        .filter(fw -> fw.getMealType() == filterType)
                        .collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid mealType: " + mealType);
            }
        }

        // DTO로 변환
        return filtered.stream()
                .map(FoodWasteResponseDTO.FoodWasteDTO::toDTO)
                .collect(Collectors.toList());
    }
}
