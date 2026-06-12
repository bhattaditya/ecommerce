package com.example.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class NotificationService {

    @Async("taskExecutor")
    public CompletableFuture<Void> sendOrderConfirmation(String email, String orderId, BigDecimal amount) {
        try {
            Thread.sleep(2000);
            System.out.printf("[%s] Email sent to %s for order %s, amount ₹%s%n",
                    Thread.currentThread().getName(), email, orderId, amount);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return CompletableFuture.completedFuture(null);
    }

    @Async("taskExecutor")
    public CompletableFuture<Void> sendPaymentFailureAlert(String email, String orderId, int attemptNumber) {
        try {
            Thread.sleep(500);
            System.out.printf("[%s] Payment failure alert sent to %s, attempt %d%n",
                    Thread.currentThread().getName(), email, attemptNumber);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return CompletableFuture.completedFuture(null);
    }
}