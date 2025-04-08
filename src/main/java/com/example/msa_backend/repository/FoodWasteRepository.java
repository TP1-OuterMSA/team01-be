package com.example.msa_backend.repository;

import com.example.msa_backend.domain.FoodWaste;
import com.example.msa_backend.domain.enums.MealType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FoodWasteRepository extends JpaRepository<FoodWaste, Long> {

    List<FoodWaste> findAllByDateBetween(LocalDate startDate, LocalDate endDate);

    FoodWaste findByDateAndMealType(LocalDate date, MealType mealType);
}
