package org.booking.saga.events;

public record PaymentFailedEvent(String bookingCode,
                                 String reason) {
}
