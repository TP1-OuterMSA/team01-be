package com.example.msa_backend.repository;

import com.example.msa_backend.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByDateBetween(LocalDate start, LocalDate end);
    Event findByDate(LocalDate date);
}