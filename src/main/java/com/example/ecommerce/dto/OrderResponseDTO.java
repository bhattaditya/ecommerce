package com.example.ecommerce.dto;

import java.math.BigDecimal;

public record OrderResponseDTO(String orderId, String message, BigDecimal amount){}
