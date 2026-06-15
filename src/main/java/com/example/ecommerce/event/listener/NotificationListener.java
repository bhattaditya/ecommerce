package com.example.ecommerce.event.listener;

import com.example.ecommerce.event.OrderPlacedEvent;
import com.example.ecommerce.event.PaymentFailedEvent;
import com.example.ecommerce.service.impl.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationListener {
    private final NotificationService notificationService;

    @Async("taskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderPlaced(OrderPlacedEvent event) {
        notificationService.sendOrderConfirmation(
                event.getEmail(),
                event.getOrderId(),
                event.getAmount()
        );
    }

    @Async("taskExecutor")
    @EventListener
    public void handlePaymentFailed(PaymentFailedEvent event) {
        notificationService.sendPaymentFailureAlert(
                event.getEmail(),
                event.getOrderId(),
                event.getAttempts()
        );
    }
}
