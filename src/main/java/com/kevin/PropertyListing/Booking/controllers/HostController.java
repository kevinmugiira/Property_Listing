package com.kevin.PropertyListing.Booking.controllers;

import com.kevin.PropertyListing.Booking.dto.BookingDtos.BookingResponse;
import com.kevin.PropertyListing.Booking.dto.ListingDtos.*;
import com.kevin.PropertyListing.Booking.services.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/api/host")
public class HostController {

    private final ListingService listings;
    private final BookingService bookings;
    private final CurrentUserService currentUser;

    public HostController(ListingService listings, BookingService bookings, CurrentUserService currentUser) {
        this.listings = listings;
        this.bookings = bookings;
        this.currentUser = currentUser;
    }

    @PostMapping("/listings")
    @ResponseStatus(HttpStatus.CREATED)
    public ListingResponse create(Authentication auth, @Valid @RequestBody ListingRequest req) {
        return listings.create(currentUser.id(auth), req);
    }

    @GetMapping("/listings")
    public Page<ListingResponse> mine(Authentication auth,
        @RequestParam(defaultValue="0") int page,
        @RequestParam(defaultValue="20") int size) {
        if (page < 0 || size < 1 || size > 100) throw new IllegalArgumentException("page must be >= 0 and size must be 1..100");
        return listings.mine(currentUser.id(auth), page, size);
    }
    @PutMapping("/listings/{id}") public ListingResponse update(Authentication auth, @PathVariable Long id,
        @Valid @RequestBody ListingRequest req) {
        return listings.update(currentUser.id(auth), id, req);
    }
    @GetMapping("/bookings") public List<BookingResponse> bookingsForMyListings(Authentication auth) {
        return bookings.hostBookings(currentUser.id(auth));
    }
}
