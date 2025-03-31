package com.example.onlinecourses.dtos.models;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PermissionDTO {
    private Long id;
    private String name;
    private String description;
    // Getters and setters
}
