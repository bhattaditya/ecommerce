package com.example.ecommerce.controller;

import com.example.ecommerce.dto.OrderRequestDTO;
import com.example.ecommerce.dto.OrderResponseDTO;
import com.example.ecommerce.service.OrderService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(value = "/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @RateLimiter(name = "orderService", fallbackMethod = "orderRateLimitFallback")
    public ResponseEntity<OrderResponseDTO> placeOrder(@RequestBody OrderRequestDTO orderRequestDTO){
        OrderResponseDTO orderResponseDTO = orderService.placeOrder(orderRequestDTO);
        return ResponseEntity.ok(orderResponseDTO);
    }

    public ResponseEntity<OrderResponseDTO> orderRateLimitFallback(OrderRequestDTO dto, Exception ex) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(null);
    }
}
