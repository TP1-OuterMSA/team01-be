package com.example.msa_backend.service.weather;

import com.example.msa_backend.domain.AtePeople;
import com.example.msa_backend.domain.PaymentLog;
import com.example.msa_backend.domain.enums.MealType;
import com.example.msa_backend.repository.AtePeopleRepository;
import com.example.msa_backend.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class WeatherSchedule {

    private final WeatherService weatherService;
    private final AtePeopleRepository atePeopleRepository;
    private final PaymentRepository paymentLogRepository;



//    @Scheduled(cron = "*/10 * * * * *", zone = "Asia/Seoul")
    @Scheduled(cron = "0 0 9,12,18 * * *", zone = "Asia/Seoul")
    public void fetchWeatherAndUpdatePeople() {
        LocalDate today = LocalDate.now();
        int hour = LocalTime.now().getHour();

        // 1. 날씨 정보 수집
        String[] weatherData = weatherService.getWeatherFromMA(59, 126);

        // 2. 시간에 따른 MealType 결정
        MealType mealType = switch (hour) {
            case 9 -> MealType.BREAKFAST;
            case 12 -> MealType.LUNCH;
            case 18 -> MealType.DINNER;
            default -> null;
        };

        // 3. PaymentLog 조회 → 갯수 집계
        if (mealType != null) {
            List<PaymentLog> logs = paymentLogRepository.findAllByDateAndMealType(today, mealType);
            long totalPeople = logs.stream().mapToLong(PaymentLog::getAmount).sum();

            LocalTime fixedTime = switch (mealType) {
                case BREAKFAST -> LocalTime.of(9, 0);
                case LUNCH -> LocalTime.of(12, 0);
                case DINNER -> LocalTime.of(18, 0);
            };

            List<AtePeople> matches = atePeopleRepository.findAllByDateAndMealType(today, mealType);
            AtePeople target;

            if (!matches.isEmpty()) {
                target = matches.get(0); // 또는 가장 오래된, 가장 최신 등 기준 적용
                target.setPeople(totalPeople);
                atePeopleRepository.save(target);
                log.info("🔁 AtePeople 갱신: {} {} → {}명", today, mealType, totalPeople);
            } else {
                target = AtePeople.builder()
                        .date(today)
                        .time(fixedTime)
                        .mealType(mealType)
                        .people(totalPeople)
                        .build();
                atePeopleRepository.save(target);
                log.info("🆕 AtePeople 저장: {} {} → {}명", today, mealType, totalPeople);
            }
        }

        // 4. 날씨 로그 출력
        if (weatherData != null) {
            log.info("🌤 날씨 정보: {} {} {} {}℃ {}%",
                    weatherData[0], weatherData[1], weatherData[2], weatherData[3], weatherData[4]);
        } else {
            log.warn("🌧 날씨 정보 수집 실패");
        }
    }

    @Scheduled(cron = "0 0 21 * * *", zone = "Asia/Seoul")
    public void scheduledShortTermForecast() {
        System.out.println("🕘 [스케줄링] 단기 예보 수집 시작");
        weatherService.getShortTermForecastFromMA(59, 126); // 서대문구 남가좌동 기준
    }
}
