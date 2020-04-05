package com.company.collabcode.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthenticationHelper {
    public static final boolean isUserLoggedIn() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal() instanceof UserDetails;
    }
}
