package com.example.ecommerce.dto;

public record OrderRequestDTO(long productId, int requestedQuantity, String paymentMethod) {
}
