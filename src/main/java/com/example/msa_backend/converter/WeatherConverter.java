package com.example.msa_backend.converter;

import com.example.msa_backend.domain.enums.Weather;

public class WeatherConverter {

    public static Weather fromStatus(String status) {
        if (status == null) {
            throw new IllegalArgumentException("status 값이 null입니다.");
        }
        return switch (status) {
            case "맑음" -> Weather.SUNNY;
            case "구름많음", "흐림" -> Weather.CLOUDY;
            case "비", "비/눈", "빗방울", "빗방울눈날림" -> Weather.RAINY;
            case "눈", "눈날림" -> Weather.SNOWY;
            default -> null; // 또는 throw new IllegalArgumentException("알 수 없는 상태: " + status);
        };
    }

}

