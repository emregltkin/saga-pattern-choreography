package org.saga.booking.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Set;

public record BookingRequest(

        @NotBlank(message = "userId is required")
        String userId,

        @NotBlank(message = "showId is required")
        String showId,

        @NotEmpty(message = "At least one seat must be selected")
        @Size(max = 10, message = "A maximum of 10 seats can be selected")
        Set<String> seatIds,

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
        @Digits(integer = 8, fraction = 2, message = "Invalid amount format")
        BigDecimal totalAmount
) {
}
