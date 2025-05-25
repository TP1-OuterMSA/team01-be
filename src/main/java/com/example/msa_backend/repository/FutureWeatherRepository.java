package com.example.msa_backend.repository;

import com.example.msa_backend.domain.FutureWeatherLog;
import com.example.msa_backend.domain.WeatherLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface FutureWeatherRepository extends JpaRepository<FutureWeatherLog, Long> {
    FutureWeatherLog findTopByDate(LocalDate date);
    List<WeatherLog> findAllByDateBetween(LocalDate start, LocalDate end);
}
