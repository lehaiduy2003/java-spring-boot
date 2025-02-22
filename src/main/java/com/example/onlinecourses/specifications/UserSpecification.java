package com.example.onlinecourses.specifications;

import com.example.onlinecourses.models.Role;
import com.example.onlinecourses.models.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

/**
 * This class contains specifications for the User entity.
 * Specifications are used to filter data from the database.
 * Each method in this class returns a Specification object.
 * Specifications are used in the JpaRepository interface to filter data.
 */
public class UserSpecification {
    /**
     * This method returns a Specification object that filters users by their username.
     * @param username The username of the user.
     * @return A Specification object that filters users by their username.
     */
    public static Specification<User> hasUsername(String username) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("username"), username);
    }

    /**
     * This method returns a Specification object that filters users by their email.
     * @param email The encrypted email of the user.
     * @return A Specification object that filters users by their email.
     */
    public static Specification<User> hasEmail(String email) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("email"), email);
    }

    /**
     * This method returns a Specification object that filters users by their phone number.
     * @param phoneNumber The encrypted phone number of the user.
     * @return A Specification object that filters users by their phone number.
     */
    public static Specification<User> hasPhoneNumber(String phoneNumber) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("phoneNumber"), phoneNumber);
    }

    /**
     * This method returns a Specification object that filters users by their role.
     * @param role The role of the user.
     * @return A Specification object that filters users by their role.
     */
    public static Specification<User> hasRole(String role) {
        return (root, query, criteriaBuilder) -> {
            Join<User, Role> join = root.join("roles");
            return criteriaBuilder.equal(join.get("name"), role);
        };
    }
}

