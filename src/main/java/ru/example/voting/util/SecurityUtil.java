package ru.example.voting.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.example.voting.service.CustomUserDetailsService;

@Component
public class SecurityUtil {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityUtil(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    public Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            String email = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
            return customUserDetailsService.getUserIdByEmail(email);
        }
        throw new IllegalStateException("User is not authenticated");
    }

}