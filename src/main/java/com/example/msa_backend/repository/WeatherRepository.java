package com.example.msa_backend.repository;

import com.example.msa_backend.domain.WeatherLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherRepository extends JpaRepository<WeatherLog, Long> {
}
