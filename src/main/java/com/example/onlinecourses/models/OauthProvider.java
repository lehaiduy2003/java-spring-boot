package com.example.onlinecourses.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "oauth_providers")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OauthProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private String provider;
    @Column(name = "linked_email", nullable = false)
    private String linkedEmail;
    @Column(name = "open_id", nullable = false, unique = true)
    private String openId;
}
