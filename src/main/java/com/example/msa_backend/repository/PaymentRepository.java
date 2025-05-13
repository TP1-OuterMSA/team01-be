package com.example.msa_backend.repository;

import com.example.msa_backend.domain.AtePeople;
import com.example.msa_backend.domain.PaymentLog;
import com.example.msa_backend.domain.enums.MealType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentLog, Long> {

}
