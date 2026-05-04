package org.saga.booking.dto;

import org.saga.booking.enums.BookingStatus;

public record BookingResponse(
        String bookingCode,
        BookingStatus bookingStatus
) {
}
