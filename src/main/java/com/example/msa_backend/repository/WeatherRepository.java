package com.example.msa_backend.repository;

import com.example.msa_backend.domain.WeatherLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;

public interface WeatherRepository extends JpaRepository<WeatherLog, Long> {
    WeatherLog findTopByDateAndTime(LocalDate date, LocalTime time);
    WeatherLog findTopByDate(LocalDate date);
}
