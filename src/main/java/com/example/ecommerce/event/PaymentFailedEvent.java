package com.example.ecommerce.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PaymentFailedEvent extends ApplicationEvent {
    private final String email;
    private final int attempts;
    private final String orderId;

    public PaymentFailedEvent(Object source, String email, int attempts, String orderId) {
        super(source);
        this.email = email;
        this.attempts = attempts;
        this.orderId = orderId;
    }
}
