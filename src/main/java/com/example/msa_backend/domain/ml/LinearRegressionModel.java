package com.example.msa_backend.domain.ml;

import java.util.List;

public class LinearRegressionModel {
    private double a, b, c, d, e, f; // f는 절편

    public void train(List<DataPoint> data) {
        int n = data.size();

        double sumX1 = 0, sumX2 = 0, sumX3 = 0, sumX4 = 0, sumX5 = 0;
        double sumY = 0;

        double sumX1X1 = 0, sumX2X2 = 0, sumX3X3 = 0, sumX4X4 = 0, sumX5X5 = 0;
        double sumX1Y = 0, sumX2Y = 0, sumX3Y = 0, sumX4Y = 0, sumX5Y = 0;

        for (DataPoint dp : data) {
            double x1 = dp.weekday;
            double x2 = dp.eventParticipants;
            double x3 = dp.temperature;
            double x4 = dp.humidity;
            double x5 = dp.weatherCode;
            double y = dp.label;

            sumX1 += x1; sumX2 += x2; sumX3 += x3; sumX4 += x4; sumX5 += x5;
            sumY += y;

            sumX1X1 += x1 * x1;
            sumX2X2 += x2 * x2;
            sumX3X3 += x3 * x3;
            sumX4X4 += x4 * x4;
            sumX5X5 += x5 * x5;

            sumX1Y += x1 * y;
            sumX2Y += x2 * y;
            sumX3Y += x3 * y;
            sumX4Y += x4 * y;
            sumX5Y += x5 * y;
        }

        double denominator = n;
        if (denominator == 0) throw new IllegalStateException("학습할 데이터가 없습니다.");

        a = sumX1X1 == 0 ? 0 : sumX1Y / sumX1X1; // 요일
        b = sumX2X2 == 0 ? 0 : sumX2Y / sumX2X2; // 행사
        c = sumX3X3 == 0 ? 0 : sumX3Y / sumX3X3; // 온도
        d = sumX4X4 == 0 ? 0 : sumX4Y / sumX4X4; // 습도
        e = sumX5X5 == 0 ? 0 : sumX5Y / sumX5X5; // 날씨
        f = (sumY - (a * sumX1 + b * sumX2 + c * sumX3 + d * sumX4 + e * sumX5)) / n;
    }

    public int predict(int weekday, int eventParticipants, double temperature, double humidity, int weatherCode) {
        double result = a * weekday + b * eventParticipants + c * temperature + d * humidity + e * weatherCode + f;
        return Math.max(0, (int) Math.round(result));
    }

    public String explainPrediction(int weekday, int eventParticipants, double temperature, double humidity, int weatherCode) {
        double weekdayPart = a * weekday;
        double eventPart = b * eventParticipants;
        double tempPart = c * temperature;
        double humPart = d * humidity;
        double weatherPart = e * weatherCode;

        double result = weekdayPart + eventPart + tempPart + humPart + weatherPart + f;

        return String.format("""
            🔍 예측 근거 상세:
            - 요일(%d) × %.2f = %.2f
            - 행사 인원(%d) × %.2f = %.2f
            - 기온(%.1f°C) × %.2f = %.2f
            - 습도(%.1f%%) × %.2f = %.2f
            - 날씨 코드(%d) × %.2f = %.2f
            - 상수항 = %.2f
            👉 총합 = %.2f (최종 예측 인원 수: %d)
            """,
                weekday, a, weekdayPart,
                eventParticipants, b, eventPart,
                temperature, c, tempPart,
                humidity, d, humPart,
                weatherCode, e, weatherPart,
                f,
                result, Math.round(result)
        );
    }

    public double getWeatherImpact(int weatherCode) {
        return e * weatherCode;
    }
}
