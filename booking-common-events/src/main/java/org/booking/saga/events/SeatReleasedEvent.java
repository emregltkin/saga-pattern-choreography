package org.booking.saga.events;

public record SeatReleasedEvent(
        String bookingCode,
        String reason) {
}