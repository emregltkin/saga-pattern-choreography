package org.saga.booking.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.booking.saga.constants.KafkaConstants;
import org.booking.saga.events.BookingCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookingEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishBookingCreatedEvent(BookingCreatedEvent event) {
        log.info("Publishing booking created event. bookingCode={}, datetime={}",
                event.bookingCode(), event.createdAt());

        kafkaTemplate.send(KafkaConstants.Topics.BOOKING_CREATED, event.bookingCode(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish event. bookingCode={}, error={}",
                                event.bookingCode(), ex.getMessage(), ex);
                    } else {
                        log.info("Event published successfully. bookingCode={}, partition={}, offset={}",
                                event.bookingCode(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset()
                        );
                    }
                });
    }
}
