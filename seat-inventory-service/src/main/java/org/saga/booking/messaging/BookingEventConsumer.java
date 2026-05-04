package org.saga.booking.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.booking.saga.constants.KafkaConstants;
import org.booking.saga.events.BookingCreatedEvent;
import org.saga.booking.service.SeatInventoryService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingEventConsumer {

    private final SeatInventoryService seatInventoryService;

    @KafkaListener(topics = KafkaConstants.Topics.BOOKING_CREATED, groupId = KafkaConstants.ConsumerGroups.SEAT_INVENTORY)
    public void consumeBookingEvents(BookingCreatedEvent event,
                                     @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        try {
            log.info("Consuming... Event:booking-created for bookingCode: {}  key:{}", event.bookingCode(), key);
            seatInventoryService.handleSeatsLockingOnBookingCreated(event);
        } catch (Exception e) {
            log.error("Error processing event:booking-created for bookingCode {}: {}", event.bookingCode(), e.getMessage());
        }
    }
}
