package com.example.ecommerce.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "outbox_events")
@Setter
@Getter
public class OutboxEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String eventType;
    private String orderId;
    private BigDecimal amount;
    private String email;
    private boolean processed;
    private int attemptsNumber;
    @CreationTimestamp
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;

}

