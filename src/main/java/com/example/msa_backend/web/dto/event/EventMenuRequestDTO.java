package com.example.msa_backend.web.dto.event;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

public class EventMenuRequestDTO {

    @Getter
    @RequiredArgsConstructor
    public static class AddDTO {
        private final String eventTitle;
        private final LocalDate date;
    }
}
