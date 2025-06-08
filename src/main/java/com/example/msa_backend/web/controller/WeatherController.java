package com.example.msa_backend.web.controller;

import com.example.msa_backend.service.foodwaste.FoodWasteService;
import com.example.msa_backend.service.weather.WeatherService;
import com.example.msa_backend.web.dto.food.FoodWasteRequestDTO;
import com.example.msa_backend.web.dto.food.FoodWasteResponseDTO;
import com.example.msa_backend.web.dto.weather.WeatherResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.example.msa_backend.global.Constants.ANALYTICS_TEAM_URL;

@RestController
@RequestMapping(ANALYTICS_TEAM_URL + "/weather")
@RequiredArgsConstructor
public class WeatherController{
    private final WeatherService weatherService;

    @GetMapping("/past")
    public WeatherResponseDTO.PastWeatherDTO getWeatherPast(
        @RequestParam LocalDate date
    ) {
        return weatherService.getWeather(date);
    }

    @GetMapping("/future")
    public List<WeatherResponseDTO.FutureWeatherDTO> getWeatherFrom() {
        return weatherService.getFutureWeather();
    }

}