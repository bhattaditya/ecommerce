package com.example.ecommerce.dto;

public record RegisterRequest(String name, String email,
                              String password, String role) {}