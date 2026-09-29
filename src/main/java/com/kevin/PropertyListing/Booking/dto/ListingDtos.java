package com.kevin.PropertyListing.Booking.dto;

import com.kevin.PropertyListing.Booking.models.PropertyListing;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.Instant;

public final class ListingDtos {
    private ListingDtos() {}
    public record ListingRequest(@NotBlank @Size(max=160) String title,
        @NotBlank @Size(max=4000) String description, @NotBlank @Size(max=120) String address,
        @NotBlank @Size(max=100) String city, @NotNull @DecimalMin("0.01") BigDecimal pricePerNight,
        @NotNull @Min(1) @Max(100) Integer maxGuests, Boolean active) {}

    public record ListingResponse(Long id, Long hostId, String hostName, String title, String description,
        String address, String city, BigDecimal pricePerNight, Integer maxGuests, boolean active,
        Instant createdAt, Instant updatedAt) {
        public static ListingResponse from(PropertyListing p) {
            return new ListingResponse(
                    p.getId(),
                    p.getHost().getId(),
                    p.getHost().getName(),
                    p.getTitle(),
                    p.getDescription(),
                    p.getAddress(),
                    p.getCity(),
                    p.getPricePerNight(),
                    p.getMaxGuests(),
                    p.isActive(),
                    p.getCreatedAt(),
                    p.getUpdatedAt());
        }
    }
}
