package org.booking.saga.constants;


import lombok.experimental.UtilityClass;

@UtilityClass
public class KafkaConstants {

    @UtilityClass
    public static class Topics {
        public static final String BOOKING_CREATED = "booking-created";
        public static final String SEAT_LOCKING = "seat-locking";
        public static final String SEAT_RELEASED = "seat-released";
        public static final String PAYMENT_COMPLETED = "payment-completed";
        public static final String PAYMENT_FAILED = "payment-failed";
    }

    @UtilityClass
    public static class ConsumerGroups {
        public static final String MOVIE_BOOKING = "movie-booking-event-group";
        public static final String SEAT_INVENTORY = "seat-inventory-event-group";
        public static final String PAYMENT = "payment-event-group";
    }
}
