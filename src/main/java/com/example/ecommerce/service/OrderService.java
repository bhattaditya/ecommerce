package com.example.ecommerce.service;

import com.example.ecommerce.entity.Order;

import java.math.BigDecimal;

public interface OrderService {
    BigDecimal calculateTotalOrder(Order order);
}
