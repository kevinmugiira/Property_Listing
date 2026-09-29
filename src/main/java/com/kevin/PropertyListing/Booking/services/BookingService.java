package com.kevin.PropertyListing.Booking.services;

import com.kevin.PropertyListing.Booking.dto.BookingDtos.*;
import com.kevin.PropertyListing.Booking.models.*;
import com.kevin.PropertyListing.Booking.repositories.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BookingService {
    private final BookingRepository bookings; private final PropertyRepository properties; private final UserRepository users;
    public BookingService(BookingRepository bookings, PropertyRepository properties, UserRepository users) {
        this.bookings=bookings; this.properties=properties; this.users=users;
    }
    @Transactional
    public BookingResponse create(Long guestId, Long propertyId, BookingRequest req) {
        if (req.checkIn() == null || req.checkOut() == null || !req.checkOut().isAfter(req.checkIn()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "checkOut must be after checkIn");
        if (req.checkIn().isBefore(LocalDate.now()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "checkIn cannot be in the past");
        PropertyListing property = properties.findById(propertyId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Listing not found"));
        if (!property.isActive()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Listing not found");
        if (req.guests() > property.getMaxGuests()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Guest count exceeds listing capacity");
        boolean overlap = bookings.existsByPropertyIdAndStatusAndCheckInLessThanAndCheckOutGreaterThan(
            propertyId, Booking.Status.CONFIRMED, req.checkOut(), req.checkIn());
        if (overlap) throw new ResponseStatusException(HttpStatus.CONFLICT, "Property is not available for those dates");
        AppUser guest = users.findById(guestId).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        long nights = ChronoUnit.DAYS.between(req.checkIn(), req.checkOut());
        BigDecimal total = property.getPricePerNight().multiply(BigDecimal.valueOf(nights));
        return BookingResponse.from(bookings.save(new Booking(property, guest, req.checkIn(), req.checkOut(), req.guests(), total)));
    }
    @Transactional(readOnly = true)
    public List<BookingResponse> guestBookings(Long guestId) {
        return bookings.findByGuestIdOrderByCreatedAtDesc(guestId).stream().map(BookingResponse::from).toList();
    }
    @Transactional(readOnly = true)
    public List<BookingResponse> hostBookings(Long hostId) {
        return bookings.findByPropertyHostIdOrderByCreatedAtDesc(hostId).stream().map(BookingResponse::from).toList();
    }
}
