package com.example.onlinecourses.repositories;

import com.example.onlinecourses.models.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findUserByEmailOrUsername(@NotBlank @Email(message = "Email should be valid") @Size(max = 100) String email, @Size(max = 100) String username);
    
    Optional<User> findUserByEmail(@NotBlank @Email(message = "Email should be valid") @Size(max = 100) String email);
    Optional<User> findUserByUsername(@Size(max = 100) String username);
    Optional<User> findUserByPhoneNumber(@Size(max = 11) String phoneNumber);
    
    boolean existsUserByEmail(@NotBlank @Email(message = "Email should be valid") @Size(max = 100) String email);
    boolean existsUserByPhoneNumber(@Size(max = 11) String phoneNumber);
    boolean existsUserByUsername(@Size(max = 100) String username);

    void deleteUserByEmail(@NotBlank @Email(message = "Email should be valid") @Size(max = 100) String email);
    void deleteUserByPhoneNumber(@Size(max = 11) String phoneNumber);
    void deleteUserByUsername(@Size(max = 100) String username);
    
}