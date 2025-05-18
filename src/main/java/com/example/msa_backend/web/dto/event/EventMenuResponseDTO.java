package com.example.msa_backend.web.dto.event;

import com.example.kafka_schemas.EventMenu;
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

        public static EventDTO from(EventMenu menu) {
            return EventDTO.builder()
                    .eventTitle(menu.getEventTitle())
                    .date(LocalDate.parse(menu.getDate()))
                    .build();
        }
    }
}