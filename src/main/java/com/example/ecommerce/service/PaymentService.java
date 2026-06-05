package com.example.ecommerce.service;

public interface PaymentService {
    public boolean processPayment(String orderID, String paymentMethod, double amount);
}
