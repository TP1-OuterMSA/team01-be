package com.example.msa_backend.repository;

import com.example.msa_backend.domain.WeatherLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WeatherRepository extends JpaRepository<WeatherLog, Long> {
    WeatherLog findTopByDateAndTime(String date, String time);

}
