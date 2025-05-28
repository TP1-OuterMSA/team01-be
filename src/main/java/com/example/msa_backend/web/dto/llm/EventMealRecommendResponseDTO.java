package com.example.msa_backend.web.dto.llm;

public class EventMealRecommendResponseDTO {
    private String recommendation;

    public EventMealRecommendResponseDTO(String recommendation) {
        this.recommendation = recommendation;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }
}
