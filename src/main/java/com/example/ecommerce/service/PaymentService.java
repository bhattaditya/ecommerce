package com.example.ecommerce.service;

import java.math.BigDecimal;

public interface PaymentService {
    public boolean processPayment(String orderID, String paymentMethod, BigDecimal amount);
}
