package com.example.msa_backend.web.controller;

import com.example.kafka_schemas.EventMenu;
import com.example.msa_backend.kafka.EventKafkaConsumer;
import com.example.msa_backend.web.dto.event.EventMenuResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {

    private final EventKafkaConsumer eventKafkaConsumer;


    @GetMapping("/latest")
    public ResponseEntity<?> getLatestEvent() {
        EventMenu latest = eventKafkaConsumer.getLatestEvent();
        if (latest == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("아직 수신된 Kafka 메시지가 없습니다.");
        }

        // Avro 객체 → DTO로 변환 후 반환
        return ResponseEntity.ok(EventMenuResponseDTO.EventDTO.from(latest));
    }
}