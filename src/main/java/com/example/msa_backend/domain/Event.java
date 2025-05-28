package com.example.msa_backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private LocalDate date;

    private Long people;
//
//    public static Event fromAvro(com.example.kafka_schemas.EventMenu eventMenu) {
//        return Event.builder()
//                .title(eventMenu.getEventTitle())
//                .date(LocalDate.parse(eventMenu.getDate()))
//                .build();
//    }
}