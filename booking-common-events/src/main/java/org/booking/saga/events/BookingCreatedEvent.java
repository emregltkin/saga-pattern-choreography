package org.booking.saga.events;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

public record BookingCreatedEvent(
        String bookingCode,
        String userId,
        String showId,
        BigDecimal totalAmount,
        Set<String> seatIds,
        Instant createdAt) {
}