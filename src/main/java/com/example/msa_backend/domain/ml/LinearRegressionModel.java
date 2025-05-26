package com.example.msa_backend.domain.ml;

import java.util.List;

public class LinearRegressionModel {
    private double a, b, c;

    public void train(List<DataPoint> data) {
        int n = data.size();

        double sumX1 = 0, sumX2 = 0, sumY = 0;
        double sumX1X1 = 0, sumX2X2 = 0, sumX1X2 = 0;
        double sumX1Y = 0, sumX2Y = 0;

        for (DataPoint dp : data) {
            double x1 = dp.weekday;
            double x2 = dp.eventParticipants;
            double y = dp.label;

            sumX1 += x1;
            sumX2 += x2;
            sumY += y;

            sumX1X1 += x1 * x1;
            sumX2X2 += x2 * x2;
            sumX1X2 += x1 * x2;

            sumX1Y += x1 * y;
            sumX2Y += x2 * y;
        }

        double denominator = (sumX1X1 * sumX2X2) - (sumX1X2 * sumX1X2);
        if (denominator == 0) throw new IllegalStateException("데이터가 선형적으로 독립적이지 않음");

        a = ((sumX2X2 * sumX1Y) - (sumX1X2 * sumX2Y)) / denominator;
        b = ((sumX1X1 * sumX2Y) - (sumX1X2 * sumX1Y)) / denominator;
        c = (sumY - a * sumX1 - b * sumX2) / n;
    }

    public int predict(int weekday, int eventParticipants) {
        return (int) Math.round(a * weekday + b * eventParticipants + c);
    }
}
