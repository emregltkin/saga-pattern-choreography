package org.saga.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.booking.saga.events.SeatLockingEvent;
import org.saga.booking.entity.PaymentTransaction;
import org.saga.booking.enums.PaymentStatus;
import org.saga.booking.exception.PaymentServiceException;
import org.saga.booking.messaging.PaymentEventProducer;
import org.saga.booking.repository.PaymentTransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentEventProducer paymentEventProducer;
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final static Integer MAX_LIMIT = 10_000;

    public void processPayment(SeatLockingEvent seatLockingEvent) {
        PaymentTransaction paymentTransaction = null;
        try {
            log.info("Processing payment for bookingCode: {}", seatLockingEvent.bookingCode());
            paymentTransaction = PaymentTransaction.builder()
                    .bookingCode(seatLockingEvent.bookingCode())
                    .totalAmount(seatLockingEvent.totalAmount())
                    .status(PaymentStatus.PENDING)
                    .build();
            paymentTransactionRepository.save(paymentTransaction);

            // call payment process
            this.performPaymentProcess(paymentTransaction);
            // update payment completed status
            paymentTransaction.setStatus(PaymentStatus.COMPLETED);
            paymentTransactionRepository.save(paymentTransaction);

            // success event
            paymentEventProducer.publishPaymentCompletedEvent(paymentTransaction);
            log.info("✅ Payment successful for bookingCode: {}", paymentTransaction.getBookingCode());

        } catch (Exception e) {
            log.error("❌ Payment failed for bookingCode: {}. Reason: {}", seatLockingEvent.bookingCode(), e.getMessage());
            // update payment failed status
            if (paymentTransaction != null) {
                paymentTransaction.setStatus(PaymentStatus.FAILED);
                paymentTransactionRepository.save(paymentTransaction);
            }
            String errorMessage = "Payment failed. Error: " + e.getMessage();
            paymentEventProducer.publishPaymentFailedEvent(seatLockingEvent.bookingCode(), errorMessage);
            throw e;
        }
    }

    private void performPaymentProcess(PaymentTransaction paymentTransaction) {
        // Payment Logic
        // Simulate payment failure scenario
        if (paymentTransaction.getTotalAmount().compareTo(BigDecimal.valueOf(MAX_LIMIT)) > 0) {
            log.info("Max Amount Limit: {}", MAX_LIMIT);
            throw new PaymentServiceException("Payment amount exceeds limit.");
        }
        log.info("The payment process was successful.");
    }
}
