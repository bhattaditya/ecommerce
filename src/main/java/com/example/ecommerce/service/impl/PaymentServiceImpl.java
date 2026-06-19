package com.example.ecommerce.service.impl;

import com.example.ecommerce.entity.PaymentMethod;
import com.example.ecommerce.repository.PaymentRepository;
import com.example.ecommerce.service.PaymentService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;

    @CircuitBreaker(name = "paymentService", fallbackMethod = "paymentFallback")
    public boolean processPayment(String orderID, String paymentMethod, BigDecimal amount){
        PaymentMethod paymentMethodEnum = PaymentMethod.valueOf(paymentMethod);
        //save payment details in DB
        return true;
    }

    public boolean paymentFallback(String orderId, String method,
                                   BigDecimal amount, Exception ex) {
        System.err.println("[CircuitBreaker] Payment service is DOWN: " + ex.getMessage());
        return false;
    }

}
