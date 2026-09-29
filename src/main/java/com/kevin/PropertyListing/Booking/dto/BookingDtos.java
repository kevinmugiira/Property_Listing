package com.kevin.PropertyListing.Booking.dto;

import com.kevin.PropertyListing.Booking.models.Booking;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public final class BookingDtos {
    private BookingDtos() {}
    public record BookingRequest(@NotNull LocalDate checkIn, @NotNull LocalDate checkOut,
                                 @NotNull @Min(1) @Max(100) Integer guests) {}
    public record BookingResponse(Long id, Long propertyId, String propertyTitle, Long guestId, String guestName,
        LocalDate checkIn, LocalDate checkOut, Integer guests, BigDecimal totalPrice, Booking.Status status,
        Instant createdAt) {
        public static BookingResponse from(Booking b) {
            return new BookingResponse(b.getId(), b.getProperty().getId(), b.getProperty().getTitle(),
                b.getGuest().getId(), b.getGuest().getName(), b.getCheckIn(), b.getCheckOut(), b.getGuests(),
                b.getTotalPrice(), b.getStatus(), b.getCreatedAt());
        }
    }
}
