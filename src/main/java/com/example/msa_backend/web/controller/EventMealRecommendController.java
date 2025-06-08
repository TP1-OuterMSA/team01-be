package com.example.msa_backend.web.controller;

import com.example.msa_backend.llm.OpenAIClient;
import com.example.msa_backend.web.dto.llm.EventMealRecommendRequestDTO.Request;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.msa_backend.global.Constants.ANALYTICS_TEAM_URL;

@RestController
@RequestMapping(ANALYTICS_TEAM_URL + "/api/llm")
public class EventMealRecommendController {

    private final OpenAIClient openAIClient;

    public EventMealRecommendController(OpenAIClient openAIClient) {
        this.openAIClient = openAIClient;
    }

    @PostMapping("/recommendation")
    public String getRecommendation(@RequestBody Request r) {
        StringBuilder sb = new StringBuilder();
        sb.append("날짜: ").append(r.getDate()).append("\n");
        sb.append("식사 종류: ").append(r.getMealType()).append("\n");
        sb.append("예상 인원: ").append(r.getCount()).append("명\n");
        sb.append("날씨: ").append(r.getWeather()).append(" / ").append(r.getTemperature()).append("°C\n");

        if (r.getEvents() != null && !r.getEvents().isEmpty()) {
            sb.append("행사: ").append(String.join(", ", r.getEvents())).append("\n");
        } else {
            sb.append("행사: 없음\n");
        }

        sb.append("위 정보를 참고해 운영 담당자에게 한 문장으로 운영 조언을 제공해줘.");
        return openAIClient.sendPrompt(sb.toString());
    }
}