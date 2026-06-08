package com.example.ecommerce.dto;
import java.util.List;

public record OrderRequestDTO(List<OrderItemRequest> items, String paymentMethod) {}