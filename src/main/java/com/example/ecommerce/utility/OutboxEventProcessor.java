package com.example.ecommerce.utility;

import com.example.ecommerce.entity.EventType;
import com.example.ecommerce.entity.OutboxEvent;
import com.example.ecommerce.event.PaymentFailedEvent;
import com.example.ecommerce.event.OrderPlacedEvent;
import com.example.ecommerce.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxEventProcessor {

    private final OutboxEventRepository outboxEventRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void processOutboxEvents() {
        List<OutboxEvent> unprocessed = outboxEventRepository
                .findByProcessedFalseOrderByCreatedAtAsc();

        for (OutboxEvent event : unprocessed) {
            System.out.println("[Outbox] Processing event: " + event.getEventType());

            try {

                if (event.getEventType().equals(EventType.ORDER_PLACED.name())) {
                    eventPublisher.publishEvent(new OrderPlacedEvent(
                            this,
                            event.getOrderId(),
                            event.getAmount(),
                            event.getEmail()
                    ));
                } else if (event.getEventType().equals(EventType.PAYMENT_FAILED.name())) {
                    eventPublisher.publishEvent(new PaymentFailedEvent(
                            this,
                            event.getEmail(),
                            event.getAttemptsNumber(),
                            event.getOrderId()

                    ));
                } else {
                    System.out.println("[Outbox] Unknown event type: " + event.getEventType());
                }

                event.setProcessed(true);
                event.setProcessedAt(LocalDateTime.now());
                outboxEventRepository.save(event);
            } catch (Exception e) {
                System.err.println("[Outbox] Failed to process event " + event.getId() + ": " + e.getMessage());
            }
        }
    }
}