package com.example.msa_backend.repository;

import com.example.msa_backend.domain.FutureWeatherLog;
import com.example.msa_backend.domain.WeatherLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FutureWeatherRepository extends JpaRepository<FutureWeatherLog, Long> {
    FutureWeatherLog findTopByDateAndTime(String date, String time);

}
