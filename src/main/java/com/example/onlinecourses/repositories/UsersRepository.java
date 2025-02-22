package com.example.onlinecourses.repositories;

import com.example.onlinecourses.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    @Query("SELECT u FROM User u WHERE u.email = :email OR u.username = :username OR u.phoneNumber = :phoneNumber")
    Optional<User> findByEmailOrUsernameOrPhoneNumber(@Param("email") String email, @Param("username") String username, @Param("phoneNumber") String phoneNumber);

}