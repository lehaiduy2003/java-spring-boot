package com.example.onlinecourses.utils;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {
    private static final boolean IS_SECURE = Dotenv.load().get("ENV").equals("production"); // Set secure flag for cookies based on environment
    private static final long JWT_REFRESH_EXPIRATION = Dotenv.load().get("JWT_REFRESH_EXPIRATION") != null ? Long.parseLong(Dotenv.load().get("JWT_REFRESH_EXPIRATION")) : 1000L * 60 * 60 * 24 * 7; // 7 days
    private CookieUtil() {
        throw new IllegalStateException("CookieUtil is a utility class");
    }
    public static void setCookies(HttpServletResponse response, String cookieName, String cookieValue) {
        // Set refresh token cookie
        Cookie refreshTokenCookie = new Cookie(cookieName, cookieValue);
        int maxAge = Math.toIntExact(JWT_REFRESH_EXPIRATION / 1000); // convert milliseconds to seconds
        refreshTokenCookie.setHttpOnly(true); // prevent JavaScript from accessing the cookie
        refreshTokenCookie.setSecure(IS_SECURE);
        refreshTokenCookie.setMaxAge(maxAge); // set the expiration time
        refreshTokenCookie.setPath("/api/v1/auth/refresh"); // only send to /api/v1/auth/refresh endpoint
        response.addCookie(refreshTokenCookie);
    }

    public static void deleteCookies(HttpServletResponse response, String cookieName) {
        // Delete refresh token cookie
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setSecure(IS_SECURE);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
