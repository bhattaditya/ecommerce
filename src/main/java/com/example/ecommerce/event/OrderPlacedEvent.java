package com.example.ecommerce.event;

import com.example.ecommerce.entity.Order;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.math.BigDecimal;

@Getter
public class OrderPlacedEvent extends ApplicationEvent {

    private final String orderId;
    private final BigDecimal amount;
    private final String email;

    public OrderPlacedEvent(Object source, String orderId, BigDecimal amount, String email) {
        super(source);
        this.orderId = orderId;
        this.amount = amount;
        this.email = email;
    }
}
