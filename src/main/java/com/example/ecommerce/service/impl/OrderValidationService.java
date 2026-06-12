package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.OrderItemRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

@Service
@RequiredArgsConstructor
public class OrderValidationService {

    private final Executor taskExecutor;

    public void validateOrder(String email, List<OrderItemRequest> items) {
        CompletableFuture<String> fraudCheck = CompletableFuture.supplyAsync(() -> {
            simulateDelay(300);
            System.out.println("[FraudCheck] running on: " + Thread.currentThread().getName());
            return "PASSED";
        }, taskExecutor);

        CompletableFuture<String> blacklistCheck = CompletableFuture.supplyAsync(() -> {
            simulateDelay(200);
            System.out.println("[BlacklistCheck] running on: " + Thread.currentThread().getName());
            return "PASSED";
        }, taskExecutor);

        CompletableFuture<String> velocityCheck = CompletableFuture.supplyAsync(() -> {
            simulateDelay(400);
            System.out.println("[VelocityCheck] running on: " + Thread.currentThread().getName());
            return "PASSED";
        }, taskExecutor);

        try{
            CompletableFuture.allOf(fraudCheck, blacklistCheck, velocityCheck).get();

            if (!"PASSED".equals(fraudCheck.get())) {
                throw new RuntimeException("Fraud check failed");
            }
            if (!"PASSED".equals(blacklistCheck.get())) {
                throw new RuntimeException("User is blacklisted");
            }
            if (!"PASSED".equals(velocityCheck.get())) {
                throw new RuntimeException("Too many orders placed recently");
            }
        } catch (ExecutionException e) {
            throw new RuntimeException("Validation failed: " + e.getCause().getMessage(), e);
        } catch (InterruptedException ex){
            Thread.currentThread().interrupt();
            throw new RuntimeException("Validation interrupted", ex);
        }
    }


    private void simulateDelay(long ms) {
        try { Thread.sleep(ms); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
