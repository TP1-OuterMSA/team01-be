package com.example.msa_backend.domain.ml;

public class DataPoint {
    public int weekday;              // 월:1 ~ 일:7
    public int eventParticipants;
    public double temperature;       // ℃
    public double humidity;          // %
    public int weatherCode;          // 맑음:0, 흐림:1, 비:2, 등
    public int label;                // 실제 식수 인원

    public DataPoint(int weekday, int eventParticipants, double temperature, double humidity, int weatherCode, int label) {
        this.weekday = weekday;
        this.eventParticipants = eventParticipants;
        this.temperature = temperature;
        this.humidity = humidity;
        this.weatherCode = weatherCode;
        this.label = label;
    }
}
