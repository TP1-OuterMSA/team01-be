package com.example.msa_backend.kafka;

import com.example.kafka_schemas.EventMenu;
import com.example.msa_backend.domain.Event;
import com.example.msa_backend.repository.EventRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

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

            // DB 저장
            Event event = Event.fromAvro(eventMenu);
            eventRepository.save(event);

            this.latestEvent = eventMenu;

        } catch (SerializationException e) {
            log.error("Kafka 역직렬화 실패", e);
        } catch (Exception e) {
            log.error("Kafka 수신 처리 중 예외 발생", e);
        }
    }
}