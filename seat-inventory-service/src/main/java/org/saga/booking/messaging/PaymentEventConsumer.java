package org.saga.booking.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.booking.saga.constants.KafkaConstants;
import org.booking.saga.events.PaymentCompletedEvent;
import org.booking.saga.events.PaymentFailedEvent;
import org.saga.booking.service.SeatInventoryService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventConsumer {

    private final SeatInventoryService seatInventoryService;

    @KafkaListener(topics = KafkaConstants.Topics.PAYMENT_COMPLETED, groupId = KafkaConstants.ConsumerGroups.SEAT_INVENTORY)
    public void consumePaymentCompletedEvents(PaymentCompletedEvent event,
                                              @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        try {
            log.info("Consuming... Event:payment-completed for bookingCode: {}  key:{}", event.bookingCode(), key);
            seatInventoryService.handleSeatsReservedOnPaymentCompleted(event.bookingCode());
        } catch (Exception e) {
            log.error("Error processing event:payment-completed for bookingCode {}: {}", event.bookingCode(), e.getMessage());
        }
    }

    @KafkaListener(topics = KafkaConstants.Topics.PAYMENT_FAILED, groupId = KafkaConstants.ConsumerGroups.SEAT_INVENTORY)
    public void consumePaymentFailedEvents(PaymentFailedEvent event,
                                           @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        try {
            log.info("Consuming... Event:payment-failed for bookingCode: {}  key:{}", event.bookingCode(), key);
            seatInventoryService.handleSeatsReleasedOnPaymentFailed(event.bookingCode());
        } catch (Exception e) {
            log.error("Error processing event:payment-failed for bookingCode {}: {}", event.bookingCode(), e.getMessage());
        }
    }
}
