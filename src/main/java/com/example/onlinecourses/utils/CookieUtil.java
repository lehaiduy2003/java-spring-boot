package com.example.onlinecourses.utils;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {
    private static final boolean IS_SECURE = Dotenv.load().get("ENV").equals("production"); // Set secure flag for cookies based on environment
    private CookieUtil() {
        throw new IllegalStateException("CookieUtil is a utility class");
    }
    public static void setCookies(HttpServletResponse response, String cookieName, String cookieValue) {
        // Set refresh token cookie
        Cookie refreshTokenCookie = new Cookie(cookieName, cookieValue);
        refreshTokenCookie.setHttpOnly(true); // prevent JavaScript from accessing the cookie
        refreshTokenCookie.setSecure(IS_SECURE);
        refreshTokenCookie.setPath("/"); // only send to /api/v1/auth/refresh endpoint
        response.addCookie(refreshTokenCookie);
    }

    public static void deleteCookies(HttpServletResponse response, String cookieName) {
        // Delete refresh token cookie
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
