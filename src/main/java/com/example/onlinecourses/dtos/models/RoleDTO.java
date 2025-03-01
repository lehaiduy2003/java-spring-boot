package com.example.onlinecourses.dtos.models;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    private Long id;
    private String name;
    private Set<PermissionDTO> permissions;
    // Getters and setters
}
