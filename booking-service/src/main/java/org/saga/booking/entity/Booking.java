package org.saga.booking.entity;

import jakarta.persistence.*;
import lombok.*;
import org.saga.booking.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String bookingCode;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String showId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "booking_seat",
            joinColumns = @JoinColumn(name = "booking_id"))
    @Column(name = "seat_inventory_id")
    private Set<String> seatIds;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookingStatus status;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(insertable = false)
    private Instant updatedAt;

    @Version
    private Long version;

    @PrePersist
    public void onCreate() {
        createdAt = Instant.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = Instant.now();
    }
}
