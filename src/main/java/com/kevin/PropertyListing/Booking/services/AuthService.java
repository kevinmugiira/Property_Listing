package com.kevin.PropertyListing.Booking.services;

import com.kevin.PropertyListing.Booking.dto.AuthDtos.*;
import com.kevin.PropertyListing.Booking.models.AppUser;
import com.kevin.PropertyListing.Booking.repositories.UserRepository;
import com.kevin.PropertyListing.Booking.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.AuthenticationException;

@Service
public class AuthService {

    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtService jwt;

    public AuthService(UserRepository users, PasswordEncoder encoder, AuthenticationManager authManager, JwtService jwt) {
        this.users = users;
        this.encoder = encoder;
        this.authManager = authManager;
        this.jwt = jwt;
    }

    public AuthResponse register(RegisterRequest req) {
        if (users.existsByEmailIgnoreCase(req.email())) throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already registered");
        AppUser user = users.save(
                new AppUser(req.name().trim(),
                req.email().trim().toLowerCase(),
                encoder.encode(req.password()), req.role())
        );
        return response(user);
    }

    public AuthResponse login(LoginRequest req) {
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(req.email(), req.password()));
        } catch (AuthenticationException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        AppUser user = users.findByEmailIgnoreCase(req.email()).orElseThrow();
        return response(user);
    }

    private AuthResponse response(AppUser user) {
        return new AuthResponse(jwt.generate(user.getEmail()),
                "Bearer",
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
}
