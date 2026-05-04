package org.saga.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.booking.saga.events.BookingCreatedEvent;
import org.booking.saga.events.SeatLockingEvent;
import org.booking.saga.events.SeatReleasedEvent;
import org.saga.booking.entity.SeatInventory;
import org.saga.booking.enums.SeatStatus;
import org.saga.booking.messaging.SeatInventoryEventProducer;
import org.saga.booking.repository.SeatInventoryRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeatInventoryService {

    private final SeatInventoryEventProducer seatInventoryEventProducer;
    private final SeatInventoryRepository seatInventoryRepository;

    public void handleSeatsLockingOnBookingCreated(BookingCreatedEvent event) {
        // Fetch seat inventories for the given show and seat numbers
        List<SeatInventory> seatInventoryList = seatInventoryRepository
                .findByShowIdAndSeatNumberIn(event.showId(), event.seatIds());

        // Check if all seats are available
        boolean allAvailable = seatInventoryList.stream()
                .allMatch(s -> s.getStatus() == SeatStatus.AVAILABLE);

        if (allAvailable) {
            // Update seat status to LOCKED and set current booking ID
            seatInventoryList.forEach(s -> {
                s.setStatus(SeatStatus.LOCKED);
                s.setBookingCode(event.bookingCode());
            });
            seatInventoryRepository.saveAll(seatInventoryList);
            // Publish seat reserved event
            var seatLockedEvent = new SeatLockingEvent(event.bookingCode(), event.seatIds(), event.totalAmount(), Instant.now());
            seatInventoryEventProducer.publishSeatLockingEvents(seatLockedEvent);
            log.info("Seats locking successfully for bookingCode {}", event.bookingCode());
        } else {
            log.warn("Seat locking failed for bookingCode {}. Some seats are not available.", event.bookingCode());
            var seatReleasedEvent = new SeatReleasedEvent(event.bookingCode(), "Some seats are not available. Seats failed for bookingCode " + event.bookingCode());
            seatInventoryEventProducer.publishSeatReleasedEvents(seatReleasedEvent);
        }
    }

    public void handleSeatsReservedOnPaymentCompleted(String bookingCode) {
        log.info("Reserving seats for bookingCode {}", bookingCode);
        List<SeatInventory> seatInventoryList = seatInventoryRepository.findByBookingCode(bookingCode);
        seatInventoryList.forEach(s -> {
            s.setStatus(SeatStatus.RESERVED);
        });
        seatInventoryRepository.saveAll(seatInventoryList);
        log.info("Seats reserved successfully for bookingCode {}", bookingCode);
    }

    public void handleSeatsReleasedOnPaymentFailed(String bookingCode) {
        log.info("Releasing seats for bookingCode {}", bookingCode);
        List<SeatInventory> seatInventoryList = seatInventoryRepository.findByBookingCode(bookingCode);
        seatInventoryList.forEach(s -> {
            s.setStatus(SeatStatus.AVAILABLE);
            s.setBookingCode(null);
        });
        seatInventoryRepository.saveAll(seatInventoryList);
        log.info("Seats released successfully for bookingCode {}", bookingCode);

        //send failed event to downstream (booking-service)
        var seatReleasedEvent = new SeatReleasedEvent(bookingCode, "SeatsReleasedOnPaymentFailed. Seats failed for bookingCode: " + bookingCode);
        seatInventoryEventProducer.publishSeatReleasedEvents(seatReleasedEvent);
    }
}
