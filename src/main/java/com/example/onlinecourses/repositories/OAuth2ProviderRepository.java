package com.example.onlinecourses.repositories;

import com.example.onlinecourses.models.OauthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OAuth2ProviderRepository extends JpaRepository<OauthProvider, Long> {
    Optional<OauthProvider> findByProviderId(String providerId);
}
