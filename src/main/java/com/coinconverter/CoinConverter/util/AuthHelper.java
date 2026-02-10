package com.coinconverter.CoinConverter.util;

import com.coinconverter.CoinConverter.exception.UserNotAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthHelper {

    public String getUserEmail(Authentication authentication) {
        String userEmail = null;
        if (authentication != null && authentication.isAuthenticated()) {
            userEmail = authentication.getName();
        }
        return userEmail;
    }

    public String isAuthenticated(Authentication authentication) {
        String userEmail = null;
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotAuthenticationException("User not authenticated");
        }
        userEmail = authentication.getName();
        return userEmail;
    }
}
