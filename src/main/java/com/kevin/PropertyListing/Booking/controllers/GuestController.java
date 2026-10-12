package com.kevin.PropertyListing.Booking.controllers;

import com.kevin.PropertyListing.Booking.dto.BookingDtos.*;
import com.kevin.PropertyListing.Booking.services.BookingService;
import com.kevin.PropertyListing.Booking.services.CurrentUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/api/guest")
public class GuestController {

    private final BookingService bookings;
    private final CurrentUserService currentUser;

    public GuestController(BookingService bookings, CurrentUserService currentUser) {
        this.bookings = bookings;
        this.currentUser = currentUser;
    }

    @PostMapping("/listings/{listingId}/bookings")
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse book(
            Authentication auth,
            @PathVariable Long listingId,
            @Valid @RequestBody BookingRequest req) {
        return bookings.create(currentUser.id(auth), listingId, req);
    }

    @GetMapping("/bookings")
    public List<BookingResponse> myBookings(Authentication auth) {
        return bookings.guestBookings(currentUser.id(auth));
    }
}
