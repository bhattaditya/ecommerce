package com.example.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Setter
@Getter
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    private double amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private String transactionId;;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
