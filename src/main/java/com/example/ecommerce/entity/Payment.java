package com.example.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Setter
@Getter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private String transactionId;
    private int attemptNumber;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
