package com.dailycodebuffer.orderservice.external.client;

import com.dailycodebuffer.orderservice.external.request.PaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "PAYMENT-SERVICE/payment")
public interface PaymentService {

    @PostMapping
    ResponseEntity<Long> doPayment(
            @RequestBody PaymentRequest paymentRequest
            );
}
