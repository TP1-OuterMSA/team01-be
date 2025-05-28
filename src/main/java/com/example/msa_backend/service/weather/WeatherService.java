package com.example.msa_backend.service.weather;

import com.example.msa_backend.web.dto.weather.WeatherResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface WeatherService {
    String[] getWeatherFromMA(int i, int i1);
    void getShortTermForecastFromMA(int i, int i1);

    WeatherResponseDTO.PastWeatherDTO getWeather(LocalDate date);
    List<WeatherResponseDTO.FutureWeatherDTO> getFutureWeather();
}
