package org.saga.booking.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.booking.saga.constants.KafkaConstants;
import org.booking.saga.events.SeatLockingEvent;
import org.saga.booking.service.PaymentService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SeatInventoryEventConsumer {

    private final PaymentService paymentService;

    @KafkaListener(topics = KafkaConstants.Topics.SEAT_LOCKING, groupId = KafkaConstants.ConsumerGroups.PAYMENT)
    public void consumeSeatLockingEvents(SeatLockingEvent event,
                                         @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        try {
            log.info("Consuming... Event:seat-locking for bookingCode: {}  key:{}", event.bookingCode(), key);
            paymentService.processPayment(event);
        } catch (Exception e) {
            log.error("Error processing event:seat-locking for bookingCode {}: {}", event.bookingCode(), e.getMessage());
        }
    }
}
