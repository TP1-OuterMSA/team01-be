package com.example.msa_backend.web.dto.event;

import com.example.msa_backend.domain.Event;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
public class EventMenuResponseDTO {

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class EventDTO {
        private final String eventTitle;
        private final LocalDate date;
        private final Long people;

        public static EventDTO from(Event event) {
            return EventDTO.builder()
                    .eventTitle(event.getTitle())
                    .date(event.getDate())
                    .people(event.getPeople())
                    .build();
        }
    }
}