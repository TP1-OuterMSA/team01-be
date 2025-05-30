package com.example.msa_backend.kafka;

import com.example.kafka_schemas.EventMenu;
import com.example.msa_backend.domain.Event;
import com.example.msa_backend.repository.EventRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventKafkaConsumer {

    private final EventRepository eventRepository;

    @Getter
    private volatile EventMenu latestEvent;

    @KafkaListener(topics = "event.web.crawler.updated", groupId = "service1")
    public void consume(EventMenu eventMenu) {
        try {
            log.info("Kafka 수신: title={}, date={}", eventMenu.getEventTitle(), eventMenu.getDate());

            // 랜덤 참여 인원 생성 (예: 30~100 사이)
            long people = ThreadLocalRandom.current().nextLong(80, 150);

            Event event = Event.builder()
                    .title(eventMenu.getEventTitle())
                    .date(LocalDate.parse(eventMenu.getDate()))
                    .people(people)  // 필드 추가 필요
                    .build();

            eventRepository.save(event);
            this.latestEvent = eventMenu;

        } catch (Exception e) {
            log.error("Kafka 수신 처리 중 예외 발생", e);
        }
    }
}