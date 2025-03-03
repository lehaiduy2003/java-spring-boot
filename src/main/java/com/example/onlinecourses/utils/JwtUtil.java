package com.example.onlinecourses.utils;
import com.example.onlinecourses.models.Role;
import com.example.onlinecourses.models.User;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.*;
import java.util.logging.Logger;

public class JwtUtil {
    private static final String SECRET_KEY = Dotenv.load().get("JWT_SECRET");

    private static final long JWT_EXPIRATION = 1000L * 60 * 60 * 24; // 24 hours

    private static final Logger logger = Logger.getLogger(JwtUtil.class.getName());

    private JwtUtil() {
        throw new IllegalStateException("JwtUtil is a utility class");
    }

    private static SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // Convert roles to list of strings
    private static List<String> getUserRoles(User user) {
        List<String> roles = new ArrayList<>();
        Set<Role> userRoles = user.getRoles();
        if(userRoles != null) {
            roles = userRoles.stream()
                .map(Role::getName)
                .toList();
        }
        return roles;
    }

    public static String generateAccessToken(User user) {
        List<String> roles = getUserRoles(user);
        // Set default role as GUEST if user has no roles
        if(roles.isEmpty()) {
            roles.add("GUEST");
        }
        return Jwts.builder()
            .subject(user.getEmail())
            .claim("id", user.getId())
            .claim("roles", roles)
            .issuedAt(Date.from(Instant.now()))
            .expiration(Date.from(Instant.now().plusSeconds(JWT_EXPIRATION)))
            .id(UUID.randomUUID().toString()) // Unique identifier for the token
            .signWith(getSecretKey())
            .compact();
    }

    public static String generateRefreshToken(User user) {
        return Jwts.builder()
            .subject(user.getEmail())
            .claim("id", user.getId())
            .issuedAt(Date.from(Instant.now()))
            .expiration(Date.from(Instant.now().plusSeconds(JWT_EXPIRATION * 7)))
            .id(UUID.randomUUID().toString()) // Unique identifier for the token
            .signWith(getSecretKey())
            .compact();
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (MalformedJwtException ex) {
            logger.warning("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.warning("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.warning("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.warning("JWT claims string is empty.");
        }
        return false;
    }

    public static Claims extractClaims(String token) {
        try {
            // Parse the token and extract claims
            return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        } catch (Exception e) {
            throw new JwtException("Error at extracting claims");
        }
    }

}
