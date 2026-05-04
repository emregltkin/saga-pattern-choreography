package org.saga.booking.entity;

import jakarta.persistence.*;
import lombok.*;
import org.saga.booking.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment_transaction")
public class PaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String bookingCode;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(insertable = false)
    private Instant updatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = Instant.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = Instant.now();
    }
}
