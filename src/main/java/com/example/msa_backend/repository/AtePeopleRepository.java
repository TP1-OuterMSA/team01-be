package com.example.msa_backend.repository;

import com.example.msa_backend.domain.AtePeople;
import com.example.msa_backend.domain.FoodWaste;
import com.example.msa_backend.domain.enums.MealType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AtePeopleRepository extends JpaRepository<AtePeople, Long> {

    List<AtePeople> findAllByDateBetween(LocalDate startDate, LocalDate endDate);

    Optional<AtePeople> findByDateAndMealType(LocalDate date, MealType mealType);
    List<AtePeople> findAllByDateAndMealType(LocalDate date, MealType mealType);

}
