package com.example.msa_backend.service.payment;

import com.example.msa_backend.converter.AtePeopleConverter;
import com.example.msa_backend.domain.AtePeople;
import com.example.msa_backend.domain.PaymentLog;
import com.example.msa_backend.domain.enums.MealType;
import com.example.msa_backend.domain.enums.Weather;
import com.example.msa_backend.repository.AtePeopleRepository;
import com.example.msa_backend.repository.PaymentRepository;
import com.example.msa_backend.web.dto.payment.PaymentRequestDTO;
import com.example.msa_backend.web.dto.payment.PaymentResponseDTO;
import com.example.msa_backend.web.dto.people.AtePeopleRequestDTO;
import com.example.msa_backend.web.dto.people.AtePeopleResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public PaymentResponseDTO.PaymentDTO postPayment(PaymentRequestDTO.addDTO request) {
        LocalDate date = request.getDate();

        // 1. 저장
        List<PaymentLog> savedLogs = request.getInfos().stream()
                .map(info -> PaymentLog.builder()
                        .date(date)
                        .mealtype(info.getMealType())
                        .amount(info.getAmount())
                        .build())
                .map(paymentRepository::save)
                .collect(Collectors.toList());

        // 2. mealType별 결제 건수 집계
        Map<MealType, Long> countByMealType = savedLogs.stream()
                .collect(Collectors.groupingBy(
                        PaymentLog::getMealtype,
                        Collectors.counting()
                ));

        // 3. DTO 리스트 변환 (amount 필드에 count를 double로 저장)
        List<PaymentResponseDTO.PaymentDTO.MealTypeInfo> infoList = countByMealType.entrySet().stream()
                .map(entry -> PaymentResponseDTO.PaymentDTO.MealTypeInfo.builder()
                        .mealType(entry.getKey())
                        .amount(entry.getValue().doubleValue()) // ✅ count → amount 필드에
                        .build())
                .collect(Collectors.toList());

        // 4. 반환
        return PaymentResponseDTO.PaymentDTO.toDTO(date, infoList);
    }
}

