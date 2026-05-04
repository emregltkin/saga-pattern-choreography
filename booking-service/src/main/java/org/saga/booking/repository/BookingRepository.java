package org.saga.booking.repository;

import org.saga.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Booking findByBookingCode(String bookingCode);

}
