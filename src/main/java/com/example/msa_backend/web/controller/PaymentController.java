package com.example.msa_backend.web.controller;

import com.example.msa_backend.service.payment.PaymentService;
import com.example.msa_backend.web.dto.payment.PaymentRequestDTO;
import com.example.msa_backend.web.dto.payment.PaymentResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/pay")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping()
    public PaymentResponseDTO.PaymentDTO postPayment (
            @RequestBody PaymentRequestDTO.addDTO paymentLogDTO
    ) {
        return paymentService.postPayment(paymentLogDTO);
    }
}
