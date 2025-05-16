package com.example.msa_backend.kafka;

//import com.example.kafka_schemas.EventInfo;
import lombok.Getter;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EventInfoListener {

//    @Getter
//    private volatile EventInfo latestEventInfo;
//
//    @KafkaListener(topics = "event.topic", groupId = "service1")
//    public void handleEvent(EventInfo event) {
//        this.latestEventInfo = event;
//        System.out.println("Received event info: " + event);
//    }
}