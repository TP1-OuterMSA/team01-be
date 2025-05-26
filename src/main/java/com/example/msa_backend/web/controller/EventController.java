package com.example.msa_backend.web.controller;

import com.example.msa_backend.domain.Event;
import com.example.msa_backend.repository.EventRepository;
import com.example.msa_backend.web.dto.event.EventMenuResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;



@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {

    private final EventRepository eventRepository;

    @GetMapping("/monthly")
    public ResponseEntity<?> getMonthlyEvents(
            @RequestParam int year,
            @RequestParam int month
    ) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());  // 해당 월 마지막 날

        List<Event> events = eventRepository.findByDateBetween(start, end);

        List<EventMenuResponseDTO.EventDTO> dtoList = events.stream()
                .map(EventMenuResponseDTO.EventDTO::from)
                .toList();

        return ResponseEntity.ok(dtoList);
    }
}