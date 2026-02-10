package com.coinconverter.CoinConverter.service;

import com.coinconverter.CoinConverter.dto.request.LoginRequest;
import com.coinconverter.CoinConverter.dto.request.RegisterRequest;
import com.coinconverter.CoinConverter.dto.response.AuthResponse;
import com.coinconverter.CoinConverter.entity.User;
import com.coinconverter.CoinConverter.exception.DuplicateEmailException;
import com.coinconverter.CoinConverter.exception.InvalidCredentialsException;
import com.coinconverter.CoinConverter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Register a new user in the system
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("Email already exists");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalStateException("Passwords do not match");
        }

        // Save user to the database
        User newUser = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        User saved = userRepository.save(newUser);

        // Return user information
        return AuthResponse.builder()
                .id(saved.getId())
                .email(saved.getEmail())
                .build();
    }

    // User login to the system
    //TODO Verify what this orElseThrow does correctly
    // I believe it does something like this, otherwise it throws an exception of type X
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        return AuthResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .build();
    }
}