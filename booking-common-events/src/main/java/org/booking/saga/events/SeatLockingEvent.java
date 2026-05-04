package org.booking.saga.events;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

public record SeatLockingEvent(
        String bookingCode,
        Set<String> seatIds,
        BigDecimal totalAmount,
        Instant createdAt)
{}