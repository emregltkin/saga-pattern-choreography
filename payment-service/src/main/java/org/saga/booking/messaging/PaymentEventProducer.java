package org.saga.booking.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.booking.saga.constants.KafkaConstants;
import org.booking.saga.events.PaymentCompletedEvent;
import org.booking.saga.events.PaymentFailedEvent;
import org.saga.booking.entity.PaymentTransaction;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishPaymentCompletedEvent(PaymentTransaction paymentTransaction) {
        log.info("Publishing... Event:payment-completed for bookingCode: {}", paymentTransaction.getBookingCode());
        PaymentCompletedEvent paymentEvent =
                new PaymentCompletedEvent(paymentTransaction.getBookingCode(), paymentTransaction.getId(), paymentTransaction.getTotalAmount());

        kafkaTemplate.send(KafkaConstants.Topics.PAYMENT_COMPLETED, paymentEvent.bookingCode(), paymentEvent)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Error while publishing event:payment-completed for bookingCode={}, error={}}",
                                paymentEvent.bookingCode(), ex.getMessage());
                    } else {
                        log.info("Event:payment-completed event published successfully. bookingCode={}, partition={}, offset={}",
                                paymentEvent.bookingCode(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset()
                        );
                    }
                });
    }

    public void publishPaymentFailedEvent(String bookingCode, String errorMessage) {
        log.info("Publishing... Event:payment-failed for bookingCode: {}", bookingCode);
        PaymentFailedEvent paymentFailedEvent =
                new PaymentFailedEvent(bookingCode, errorMessage);

        kafkaTemplate.send(KafkaConstants.Topics.PAYMENT_FAILED, paymentFailedEvent.bookingCode(), paymentFailedEvent)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Error while publishing event:payment-failed for bookingCode={}, error={}}",
                                paymentFailedEvent.bookingCode(), ex.getMessage());
                    } else {
                        log.info("Event:payment-failed published successfully. bookingCode={}, partition={}, offset={}",
                                paymentFailedEvent.bookingCode(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset()
                        );
                    }
                });

    }
}
