package com.example.msa_backend.web.controller;

import com.example.msa_backend.service.payment.PaymentService;
import com.example.msa_backend.web.dto.payment.PaymentRequestDTO;
import com.example.msa_backend.web.dto.payment.PaymentResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.example.msa_backend.global.Constants.ANALYTICS_TEAM_URL;


@RestController
@RequestMapping(ANALYTICS_TEAM_URL + "/pay")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/add")
    public PaymentResponseDTO.PaymentDTO postPayment (
            @RequestBody PaymentRequestDTO.addDTO paymentLogDTO
    ) {
        return paymentService.postPayment(paymentLogDTO);
    }
}
