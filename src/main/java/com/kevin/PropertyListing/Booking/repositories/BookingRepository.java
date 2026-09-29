package com.kevin.PropertyListing.Booking.repositories;


import com.kevin.PropertyListing.Booking.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByGuestIdOrderByCreatedAtDesc(Long guestId);
    List<Booking> findByPropertyHostIdOrderByCreatedAtDesc(Long hostId);
    boolean existsByPropertyIdAndStatusAndCheckInLessThanAndCheckOutGreaterThan(
        Long propertyId, Booking.Status status, LocalDate checkOut, LocalDate checkIn);
}
