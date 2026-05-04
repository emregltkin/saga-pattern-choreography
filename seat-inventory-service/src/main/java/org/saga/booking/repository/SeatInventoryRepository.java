package org.saga.booking.repository;

import org.saga.booking.entity.SeatInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface SeatInventoryRepository extends JpaRepository<SeatInventory, Long> {

    List<SeatInventory> findByShowIdAndSeatNumberIn(String showId, Set<String> seatNumbers);

    List<SeatInventory> findByBookingCode(String bookingCode);
}