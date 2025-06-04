package com.example.msa_backend.service.manage;

import com.example.msa_backend.domain.enums.MealType;
import com.example.msa_backend.service.atepeople.AtePeopleService;
import com.example.msa_backend.service.weather.WeatherService;
import com.example.msa_backend.web.dto.manage.ManageResponseDTO;
import com.example.msa_backend.web.dto.people.AtePeopleResponseDTO;
import com.example.msa_backend.web.dto.weather.WeatherResponseDTO;
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
    private final WeatherService weatherService;

    @Override
    public List<ManageResponseDTO.ManagePeopleDTO> getRecommendedStaffByDate(LocalDate date) {
        List<ManageResponseDTO.ManagePeopleDTO> result = new ArrayList<>();

        // 1. 날씨 정보 미리 조회
        WeatherResponseDTO.PastWeatherDTO weather = weatherService.getWeather(date);

        for (MealType mealType : MealType.values()) {
            try {
                AtePeopleResponseDTO.PredictPeopleWithExplanation predicted =
                        atePeopleService.getPredictPeople(date, mealType.name());

                if (predicted != null && predicted.getPeople() != null) {
                    long predictedPeople = predicted.getPeople();
                    Long availableStaff = getAvailableStaff(date, mealType);

                    String recommendation = getRecommendationMessage(
                            predictedPeople,
                            availableStaff,
                            weather.getTemperature(),
                            weather.getHumidity(),
                            weather.getStatus()
                    );

                    result.add(ManageResponseDTO.ManagePeopleDTO.toDTO(
                            date,
                            predictedPeople,
                            mealType,
                            availableStaff,
                            recommendation
                    ));
                }
            } catch (Exception e) {
                log.warn("[ManageService] {} 예측 실패: {}", mealType, e.getMessage());
            }
        }

        return result;
    }



    private String getRecommendationMessage(Long predictedPeople, Long availableStaff,
                                            double temperature, double humidity, String status) {
        long capacityPerStaff = 50L;
        long maxCapacity = capacityPerStaff * availableStaff;
        long gap = predictedPeople - maxCapacity;

        // 간단한 날씨 상태 기반 보정
        int weatherAdjustment = switch (status) {
            case "비", "눈", "폭우", "폭설" -> 20;
            case "흐림", "안개", "미세먼지" -> 10;
            case "맑음" -> 0;
            default -> 5;
        };
        gap += weatherAdjustment;

        if (gap > 100) {
            return "🚨 극심한 혼잡 예상! 날씨 영향도 큽니다. 즉각적인 인력 증원이 필요합니다.";
        } else if (gap > 50) {
            return "🔺 매우 혼잡할 것으로 예상됩니다. 날씨 조건도 고려해 조치하세요.";
        } else if (gap > 20) {
            return "⚠ 혼잡할 수 있습니다. 날씨 영향으로 상황 악화 가능성이 있습니다.";
        } else if (gap > 0) {
            return "🔸 다소 붐빌 수 있습니다. 날씨에 따른 변동도 주의하세요.";
        } else if (gap > -30) {
            return "✅ 원활한 운영이 예상됩니다.";
        } else {
            return "🟢 여유로운 운영이 가능합니다.";
        }
    }


    private Long getAvailableStaff(LocalDate date, MealType mealType) {
        // 예: DB 조회 혹은 설정된 기본값
        // 간단한 예시로 고정값 사용
        return switch (mealType) {
            case BREAKFAST -> 5L;
            case LUNCH -> 11L;
            case DINNER -> 9L;
            default -> 6L;
        };
    }
}
