package com.example.onlinecourses.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "permissions")
@Builder
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String name;

    @NotBlank
    private String description;

    @ManyToMany(mappedBy = "permissions", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Set<Role> roles; // For many-to-many relationship with Role

    public void addRole(Role role) {
        this.roles.add(role);
        role.getPermissions().add(this);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
        role.getPermissions().remove(this);
    }
}