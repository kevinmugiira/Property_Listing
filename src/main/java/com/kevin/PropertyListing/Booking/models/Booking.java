package com.kevin.PropertyListing.Booking.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "bookings", indexes = {
    @Index(name = "idx_booking_dates", columnList = "property_id,check_in,check_out"),
    @Index(name = "idx_booking_guest", columnList = "guest_id")
})
public class Booking {
    public enum Status { CONFIRMED, CANCELLED }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name="property_id", nullable=false)
    private PropertyListing property;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name="guest_id", nullable=false)
    private AppUser guest;

    @Column(name="check_in", nullable=false)
    private LocalDate checkIn;

    @Column(name="check_out", nullable=false)
    private LocalDate checkOut;

    @Column(nullable=false)
    private Integer guests;

    @Column(nullable=false, precision=12, scale=2)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=20)
    private Status status = Status.CONFIRMED;

    @Column(nullable=false, updatable=false)
    private Instant createdAt = Instant.now();

    protected Booking() {}
    public Booking(PropertyListing property, AppUser guest, LocalDate checkIn, LocalDate checkOut,
                   Integer guests, BigDecimal totalPrice) {
        this.property=property;
        this.guest=guest;
        this.checkIn=checkIn;
        this.checkOut=checkOut;
        this.guests=guests;
        this.totalPrice=totalPrice;
    }
    public void cancel(){this.status= Status.CANCELLED;}
    public Long getId(){return id;}
    public PropertyListing getProperty(){return property;}
    public AppUser getGuest(){return guest;}
    public LocalDate getCheckIn(){return checkIn;}
    public LocalDate getCheckOut(){return checkOut;}
    public Integer getGuests(){return guests;}
    public BigDecimal getTotalPrice(){return totalPrice;}
    public Status getStatus(){return status;}
    public Instant getCreatedAt(){return createdAt;}
}
