package com.example.msa_backend.service.weather;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class WeatherSchedule {

    private final WeatherService weatherService;

    public WeatherSchedule(WeatherService weatherService) {
        this.weatherService = weatherService;
    }


//        @Scheduled(cron = "0 0/1 * * * *", zone = "Asia/Seoul") // 1분마다 테스트
//        @Scheduled(cron = "*/20 * * * * *", zone = "Asia/Seoul") // ← 매 10초마다 실행
    @Scheduled(cron = "0 0 9,12,18 * * *", zone = "Asia/Seoul") //시간마다
    public void fetchWeather() {
        String[] weatherData = weatherService.getWeather(59, 126); // 서대문구 남가좌동 기준
        if (weatherData != null) {
            System.out.println("===== 날씨 예보 =====");
            System.out.println("날짜 : " + weatherData[0]);
            System.out.println("시간 : " + weatherData[1]);
            System.out.println("날씨 : " + weatherData[2]);
            System.out.println("기온 : " + weatherData[3] + "℃");
            System.out.println("습도 : " + weatherData[4] + "%");
            System.out.println("=====================");
        } else {
            System.out.println("날씨 정보 호출 실패");
        }
    }
}
