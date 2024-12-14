package com.example.onlinecourses.repositories;

import com.example.onlinecourses.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
    Optional<User> findByUsername(String username);

    void deleteByEmail(String email);
    void deleteByPhoneNumber(String phoneNumber);
}