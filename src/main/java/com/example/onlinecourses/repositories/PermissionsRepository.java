package com.example.onlinecourses.repositories;

import com.example.onlinecourses.models.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionsRepository extends JpaRepository<Permission, Integer> {
}