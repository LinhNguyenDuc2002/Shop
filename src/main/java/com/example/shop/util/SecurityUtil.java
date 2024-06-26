package com.example.shop.util;

import com.example.shop.entity.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtil {
    /**
     * @return
     */
    public static String getLoggedInUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            return null;
        }

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return userPrincipal.getId().toString();
    }
}
