package org.saga.booking.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.booking.saga.constants.KafkaConstants;
import org.booking.saga.events.SeatLockingEvent;
import org.booking.saga.events.SeatReleasedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SeatInventoryEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishSeatLockingEvents(SeatLockingEvent event) {
        log.info("Publishing... Event:seat-locking for bookingCode: {}", event.bookingCode());

        kafkaTemplate.send(KafkaConstants.Topics.SEAT_LOCKING, event.bookingCode(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Error while publishing event:seat-locking for bookingCode={}, error={}}",
                                event.bookingCode(), ex.getMessage());
                    } else {
                        log.info("Event:seat-locking published successfully. bookingCode={}, partition={}, offset={}",
                                event.bookingCode(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset()
                        );
                    }
                });
    }

    public void publishSeatReleasedEvents(SeatReleasedEvent event) {
        log.info("Publishing... Event:seat-released for bookingCode: {}", event.bookingCode());

        kafkaTemplate.send(KafkaConstants.Topics.SEAT_RELEASED, event.bookingCode(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Error while publishing event:seat-released for bookingCode={}, error={}}",
                                event.bookingCode(), ex.getMessage());
                    } else {
                        log.info("Event:seat-released published successfully. bookingCode={}, partition={}, offset={}",
                                event.bookingCode(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset()
                        );
                    }
                });
    }

}
