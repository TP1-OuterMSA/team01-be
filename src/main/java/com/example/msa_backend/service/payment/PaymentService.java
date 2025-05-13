package com.example.msa_backend.service.payment;

import com.example.msa_backend.web.dto.payment.PaymentRequestDTO;
import com.example.msa_backend.web.dto.payment.PaymentResponseDTO;
import com.example.msa_backend.web.dto.people.AtePeopleRequestDTO;
import com.example.msa_backend.web.dto.people.AtePeopleResponseDTO;

import java.time.LocalDate;

public interface PaymentService {

    PaymentResponseDTO.PaymentDTO postPayment(PaymentRequestDTO.addDTO paymentRequestDTO);

}
