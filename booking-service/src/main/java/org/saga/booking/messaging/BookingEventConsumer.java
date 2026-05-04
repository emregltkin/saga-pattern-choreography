package org.saga.booking.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.booking.saga.constants.KafkaConstants;
import org.booking.saga.events.PaymentCompletedEvent;
import org.booking.saga.events.SeatReleasedEvent;
import org.saga.booking.service.BookingService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingEventConsumer {

    private final BookingService bookingService;

    // COMPENSATING: Seats were released. → CANCELLED
    @KafkaListener(topics = KafkaConstants.Topics.SEAT_RELEASED, groupId = KafkaConstants.ConsumerGroups.MOVIE_BOOKING)
    public void consumeSeatReleasedEvents(SeatReleasedEvent event,
                                          @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        try {
            log.info("Consuming... Event:seat-released for bookingCode: {}  key:{}", event.bookingCode(), key);
            bookingService.cancelBooking(event.bookingCode(), event.reason());
        } catch (Exception e) {
            log.error("Error processing event:seat-released for bookingCode {}: {}", event.bookingCode(), e.getMessage());
        }
    }

    @KafkaListener(topics = KafkaConstants.Topics.PAYMENT_COMPLETED, groupId = KafkaConstants.ConsumerGroups.MOVIE_BOOKING)
    public void consumePaymentCompletedEvents(PaymentCompletedEvent event,
                                              @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        try {
            log.info("Consuming... Event:payment-completed for bookingCode: {}  key:{}", event.bookingCode(), key);
            bookingService.completeBooking(event.bookingCode());
        } catch (Exception e) {
            log.error("Error processing event:payment-completed for bookingCode {}: {}", event.bookingCode(), e.getMessage());
        }
    }
}
