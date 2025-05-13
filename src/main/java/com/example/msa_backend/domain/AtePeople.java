package com.example.msa_backend.domain;

import com.example.msa_backend.domain.enums.MealType;
import com.example.msa_backend.domain.enums.Weather;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity
@Getter
@Builder
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AtePeople {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate date;

    @DateTimeFormat(pattern = "HH:mm")
    @Column(nullable = false)
    private LocalTime time;

    @Setter
    @Column(nullable = false)
    private Long people;

    @Enumerated(EnumType.STRING)
    @Column(name = "meal_type", nullable = false)
    private MealType mealType;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "weather", nullable = false)
    private Weather weather;

    @PrePersist
    public void setTimeByMealType() {

        // 식사 타입에 따라 시간 자동 지정
        if (this.time == null) {
            switch (this.mealType) {
                case BREAKFAST -> this.time = LocalTime.of(9, 0);
                case LUNCH     -> this.time = LocalTime.of(12, 0);
                case DINNER    -> this.time = LocalTime.of(18, 0);
            }
        }
    }
}
