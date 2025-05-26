package com.example.msa_backend.domain.ml;

public class DataPoint {
    public int weekday; // 월:1 ~ 일:7
    public int eventParticipants;
    public int label; // 실제 식수 인원

    public DataPoint(int weekday, int eventParticipants, int label) {
        this.weekday = weekday;
        this.eventParticipants = eventParticipants;
        this.label = label;
    }
}
