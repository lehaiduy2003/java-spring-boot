package com.example.onlinecourses.providers;

import com.example.onlinecourses.models.Role;
import com.example.onlinecourses.models.User;
import com.example.onlinecourses.repositories.RolesRepository;
import com.example.onlinecourses.repositories.UsersRepository;
import com.example.onlinecourses.specifications.UserDetailsImpl;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtProvider {
    private static final String SECRET_KEY = Dotenv.load().get("JWT_SECRET");

    private final long JWT_EXPIRATION = 1000L * 60 * 60 * 24; // 24 hours

    private final UsersRepository usersRepository;

    public JwtProvider(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }


    private static SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateAccessToken(Authentication authentication) {
        if (authentication.getPrincipal() instanceof UserDetailsImpl user) {
            List<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
            return Jwts.builder()
                .subject(Long.toString(user.getId()))
                .claim("roles", roles)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(JWT_EXPIRATION)))
                .id(UUID.randomUUID().toString()) // Unique identifier for the token
                .signWith(getSecretKey())
                .compact();
        } else {
            throw new IllegalArgumentException("Principal must be an instance of UserDetailsImpl");
        }
    }

    public String generateRefreshToken(Authentication authentication) {
        if (authentication.getPrincipal() instanceof UserDetailsImpl user) {
            return Jwts.builder()
                .subject(Long.toString(user.getId()))
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(JWT_EXPIRATION * 7)))
                .id(UUID.randomUUID().toString()) // Unique identifier for the token
                .signWith(getSecretKey())
                .compact();
        } else {
            throw new IllegalArgumentException("Principal must be an instance of UserDetailsImpl");
        }
    }

    public String refreshAccessToken(String refreshToken) {
        try {
            // Validate refresh token
            Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(refreshToken)
                .getPayload();

            // Check expiration
            if (claims.getExpiration().before(new Date())) {
                throw new JwtException("Refresh token has expired");
            }

            // extract identifier from token
            String userId = claims.getSubject();
            User user = usersRepository.findById(Long.parseLong(userId)).orElseThrow(() -> new RuntimeException("User not found"));
            List<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());

            // Tạo token mới
            return Jwts.builder()
                .subject(userId)
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(Date.from(Instant.now().plusSeconds(JWT_EXPIRATION)))
                .signWith(getSecretKey())
                .compact();

        } catch (JwtException e) {
            throw new JwtException("Invalid refresh token");
        }
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (MalformedJwtException ex) {
            System.out.println("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            System.out.println("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            System.out.println("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            System.out.println("JWT claims string is empty.");
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

    public static Authentication getAuthentication(String token) {
        Claims claims = extractClaims(token);
        String userId = claims.getSubject();
        List<GrantedAuthority> authorities = extractAuthorities(claims);

        // Create an Authentication object
        return new UsernamePasswordAuthenticationToken(
            userId,      // Principal (user identity)
            null,          // Credentials (usually null)
            authorities    // User roles/permissions
        );
    }

    private static List<GrantedAuthority> extractAuthorities(Claims claims) {
        List<String> roles = claims.get("roles", List.class);
        return roles.stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    }
}
