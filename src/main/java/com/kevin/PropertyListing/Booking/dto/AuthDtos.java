package com.kevin.PropertyListing.Booking.dto;

import com.kevin.PropertyListing.Booking.models.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public final class AuthDtos {
    private AuthDtos() {}
    public record RegisterRequest(@NotBlank @Size(max=120) String name,
        @NotBlank @Email @Size(max=190) String email,
        @NotBlank @Size(min=8, max=72) String password,
        @NotNull Role role) {}
    public record LoginRequest(@NotBlank @Email String email, @NotBlank String password) {}
    public record AuthResponse(String token, String tokenType, Long userId, String name, String email, Role role) {}
}
