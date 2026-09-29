package com.kevin.PropertyListing.Booking.controllers;

import com.kevin.PropertyListing.Booking.dto.ListingDtos.*;
import com.kevin.PropertyListing.Booking.services.ListingService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/listings")
@Tag(
        name = "Property Listings",
        description = "Public endpoints for discovering properties"
)
public class ListingController {


    private final ListingService listings;
    public ListingController(ListingService listings) {
        this.listings=listings;
    }

    @Operation(
            summary = "Browse properties",
            description = "Returns active properties with optional city filtering and pagination"
    )
    @GetMapping
    public Page<ListingResponse> browse(
            @RequestParam(required=false) String city,
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="20") int size) {
        if (page < 0 || size < 1 || size > 100) throw new IllegalArgumentException("page must be >= 0 and size must be 1..100");
        return listings.browse(city, page, size);
    }
    @GetMapping("/{id}")
    public ListingResponse get(@PathVariable Long id) { return listings.getPublic(id); }
}
