package com.kevin.PropertyListing.Booking.repositories;

import com.kevin.PropertyListing.Booking.models.PropertyListing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PropertyRepository extends JpaRepository<PropertyListing, Long> {

    @Override
    @EntityGraph(attributePaths = {"host"})
    Optional<PropertyListing> findById(Long id);

    @EntityGraph(attributePaths = {"host"})
    Page<PropertyListing> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"host"})
    Page<PropertyListing> findByActiveTrueAndCityContainingIgnoreCase(String city, Pageable pageable);

    @EntityGraph(attributePaths = {"host"})
    Page<PropertyListing> findByActiveTrue(Pageable pageable);

    @EntityGraph(attributePaths = {"host"})
    Page<PropertyListing> findByHostId(Long hostId, Pageable pageable);

    @EntityGraph(attributePaths = {"host"})
    Optional<PropertyListing> findByIdAndHostId(Long id, Long hostId);
}
