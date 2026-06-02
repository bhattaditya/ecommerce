package com.example.ecommerce.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String name;
    private String password;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String role;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
