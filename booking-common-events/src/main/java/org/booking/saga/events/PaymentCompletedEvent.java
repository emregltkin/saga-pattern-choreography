package org.booking.saga.events;

import java.math.BigDecimal;

public record PaymentCompletedEvent(
        String bookingCode,
        Long paymentTransactionId,
        BigDecimal totalAmount
) {
}
