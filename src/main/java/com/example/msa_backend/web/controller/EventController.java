package com.example.msa_backend.web.controller;

//import com.example.kafka_schemas.EventInfo;
import com.example.msa_backend.kafka.EventInfoListener;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {

    private final EventInfoListener eventInfoListener;

//    @GetMapping("/latest")
//    public EventInfo getLatestEvent() {
//        return eventInfoListener.getLatestEventInfo();
//    }
}