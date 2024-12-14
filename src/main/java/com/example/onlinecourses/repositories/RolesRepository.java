package com.example.onlinecourses.repositories;
import com.example.onlinecourses.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<Role, Integer> {
}