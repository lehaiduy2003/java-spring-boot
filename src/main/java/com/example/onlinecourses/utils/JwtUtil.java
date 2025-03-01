package com.example.onlinecourses.utils;
import com.example.onlinecourses.models.Role;
import com.example.onlinecourses.models.User;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
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

    public static String generateAccessToken(User user) {
        List<Role> roles = new ArrayList<>();
        if(user.getRoles() == null) {
            Role role = Role.builder()
                .name("GUEST")
                .build();
            roles.add(role);
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
