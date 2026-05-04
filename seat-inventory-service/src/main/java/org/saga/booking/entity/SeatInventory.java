package org.saga.booking.entity;

import jakarta.persistence.*;
import lombok.*;
import org.saga.booking.enums.SeatStatus;

import java.time.Instant;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "seat_inventory")
public class SeatInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "theater_id", nullable = false)
    private String theaterId;

    @Column(name = "screen_id", nullable = false)
    private String screenId;

    @Column(name = "show_id", nullable = false)
    private String showId;

    @Column(name = "seat_number", nullable = false, length = 10)
    private String seatNumber;

    @Column(name = "booking_code", length = 50)
    private String bookingCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SeatStatus status;

    @Column(name = "last_updated")
    private Instant lastUpdated;

    @PrePersist
    @PreUpdate
    public void onUpdate() {
        lastUpdated = Instant.now();
    }
}
