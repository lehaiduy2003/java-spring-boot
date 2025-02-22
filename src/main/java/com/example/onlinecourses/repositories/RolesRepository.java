package com.example.onlinecourses.repositories;
import com.example.onlinecourses.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface RolesRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String roleName);

    Set<Role> findByNameIn(Collection<String> names);
}