package com.example.msa_backend.web.dto.weather;

import com.example.msa_backend.domain.AtePeople;
import com.example.msa_backend.domain.WeatherLog;
import com.example.msa_backend.domain.enums.MealType;
import com.example.msa_backend.domain.enums.Weather;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
public class WeatherResponseDTO {

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class FutureWeatherDTO {
        private final LocalDate date;
        private final String status;      // 맑음, 흐림 등
        private final Double temperature; // ℃
        private final Double humidity;    // %

        public static FutureWeatherDTO toDTO(WeatherLog weather) {
            return FutureWeatherDTO.builder()
                    .date(weather.getDate())
                    .status(weather.getStatus())
                    .temperature(weather.getTemperature())
                    .humidity(weather.getHumidity())
                    .build();
        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class PastWeatherDTO {
        private final LocalDate date;
        private final String status;      // 맑음, 흐림 등
        private final Double temperature; // ℃
        private final Double humidity;    // %

        public static PastWeatherDTO toDTO(WeatherLog weather) {
            return PastWeatherDTO.builder()
                    .date(weather.getDate())
                    .status(weather.getStatus())
                    .temperature(weather.getTemperature())
                    .humidity(weather.getHumidity())
                    .build();
        }
    }
}
