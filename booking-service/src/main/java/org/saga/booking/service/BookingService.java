package org.saga.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.booking.saga.events.BookingCreatedEvent;
import org.saga.booking.dto.BookingRequest;
import org.saga.booking.dto.BookingResponse;
import org.saga.booking.entity.Booking;
import org.saga.booking.enums.BookingStatus;
import org.saga.booking.generator.ReservationCodeGenerator;
import org.saga.booking.messaging.BookingEventProducer;
import org.saga.booking.repository.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingEventProducer bookingEventProducer;

    @Transactional
    public BookingResponse createBooking(BookingRequest request) {
        String bookingCode = ReservationCodeGenerator.generate();
        log.info("Booking seats for user-id: {} - show-id: {} - booking-code: {}",
                request.userId(), request.showId(), bookingCode);

        Booking bookingEntity = Booking.builder()
                .userId(request.userId())
                .showId(request.showId())
                .bookingCode(bookingCode)
                .totalAmount(request.totalAmount())
                .seatIds(request.seatIds())
                .status(BookingStatus.CREATED)
                .build();
        var savedBooking = bookingRepository.save(bookingEntity);

        var bookingCreatedEvent = buildBookingCreateEvent(savedBooking);
        bookingEventProducer.publishBookingCreatedEvent(bookingCreatedEvent);

        log.info("Seats confirmed with booking code: {}", bookingCode);
        return new BookingResponse(bookingCode, savedBooking.getStatus());
    }

    private BookingCreatedEvent buildBookingCreateEvent(Booking savedBooking) {
        return new BookingCreatedEvent(
                savedBooking.getBookingCode(),
                savedBooking.getUserId(),
                savedBooking.getShowId(),
                savedBooking.getTotalAmount(),
                savedBooking.getSeatIds(),
                Instant.now()
        );
    }

    @Transactional
    public void cancelBooking(String bookingCode, String reason) {
        Booking bookingEntity = bookingRepository.findByBookingCode(bookingCode);
        if (bookingEntity != null) {
            bookingEntity.setStatus(BookingStatus.CANCELLED);
            bookingRepository.save(bookingEntity);
            log.info("Booking marked as CANCELLED for bookingCode: {} - reason: {}", bookingCode, reason);
        } else {
            log.warn("No booking found with bookingCode {}", bookingCode);
        }
    }

    @Transactional
    public void completeBooking(String bookingCode) {
        Booking bookingEntity = Optional.ofNullable(bookingRepository.findByBookingCode(bookingCode))
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with code: " + bookingCode));
        bookingEntity.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(bookingEntity);
    }
}
