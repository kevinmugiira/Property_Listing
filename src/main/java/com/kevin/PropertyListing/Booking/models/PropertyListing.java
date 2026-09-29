package com.kevin.PropertyListing.Booking.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "property_listings", indexes = {
    @Index(name = "idx_listing_city", columnList = "city"),
    @Index(name = "idx_listing_available", columnList = "active")
})
public class PropertyListing {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "host_id", nullable = false)
    private AppUser host;

    @Column(nullable = false, length = 160)
    private String title;

    @Column(nullable = false, length = 4000)
    private String description;

    @Column(nullable = false, length = 120)
    private String address;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal pricePerNight;

    @Column(nullable = false)
    private Integer maxGuests;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    protected PropertyListing() {}
    public PropertyListing(AppUser host, String title, String description, String address, String city,
                           BigDecimal pricePerNight, Integer maxGuests) {
        this.host=host; this.title=title; this.description=description; this.address=address; this.city=city;
        this.pricePerNight=pricePerNight; this.maxGuests=maxGuests;
    }
    public void update(String title, String description, String address, String city, BigDecimal pricePerNight,
                       Integer maxGuests, boolean active) {
        this.title=title; this.description=description; this.address=address; this.city=city;
        this.pricePerNight=pricePerNight; this.maxGuests=maxGuests; this.active=active; this.updatedAt=Instant.now();
    }
    public Long getId(){return id;}
    public AppUser getHost(){return host;}
    public String getTitle(){return title;}
    public String getDescription(){return description;}
    public String getAddress(){return address;}
    public String getCity(){return city;}
    public BigDecimal getPricePerNight(){return pricePerNight;}
    public Integer getMaxGuests(){return maxGuests;}
    public boolean isActive(){return active;}
    public Instant getCreatedAt(){return createdAt;}
    public Instant getUpdatedAt(){return updatedAt;}
}
