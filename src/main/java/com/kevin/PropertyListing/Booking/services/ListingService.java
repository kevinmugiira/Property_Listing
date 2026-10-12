package com.kevin.PropertyListing.Booking.services;

import com.kevin.PropertyListing.Booking.dto.ListingDtos.*;
import com.kevin.PropertyListing.Booking.models.*;
import com.kevin.PropertyListing.Booking.repositories.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListingService {
    private final PropertyRepository properties;
    private final UserRepository users;

    public ListingService(PropertyRepository properties, UserRepository users) {
        this.properties = properties;
        this.users = users;
    }

    public Page<ListingResponse> browse(String city, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return (city == null || city.isBlank() ? properties.findByActiveTrue(pageable)
            : properties.findByActiveTrueAndCityContainingIgnoreCase(city.trim(), pageable)).map(ListingResponse::from);
    }
    public ListingResponse getPublic(Long id) {
        PropertyListing p = properties.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Listing not found"));
        if (!p.isActive()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Listing not found");
        return ListingResponse.from(p);
    }
    public ListingResponse create(Long hostId, ListingRequest req) {
        AppUser host = users.findById(hostId).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        PropertyListing p = properties.save(new PropertyListing(host, req.title().trim(), req.description().trim(),
            req.address().trim(), req.city().trim(), req.pricePerNight(), req.maxGuests()));
        return ListingResponse.from(p);
    }
    public Page<ListingResponse> mine(Long hostId, int page, int size) {
        return properties.findByHostId(hostId,
                PageRequest
                        .of(page, size, Sort.by("createdAt")
                                .descending()))
                                        .map(ListingResponse::from);
    }

    @Transactional
    public ListingResponse update(Long hostId, Long listingId, ListingRequest req) {
        PropertyListing p = properties.findByIdAndHostId(listingId, hostId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Listing not found"));
        p.update(req.title().trim(),
                req.description().trim(),
                req.address().trim(),
                req.city().trim(),
            req.pricePerNight(),
                req.maxGuests(),
                req.active() == null || req.active());
        return ListingResponse.from(p);
    }
}
