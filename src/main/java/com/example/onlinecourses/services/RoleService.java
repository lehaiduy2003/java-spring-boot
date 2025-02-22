package com.example.onlinecourses.services;

import com.example.onlinecourses.models.Role;
import com.example.onlinecourses.repositories.RolesRepository;
import com.example.onlinecourses.services.Interfaces.IRoleService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RoleService implements IRoleService {
    private final RolesRepository rolesRepository;

    public RoleService(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    @Override
    public Role findRoleByName(String roleName) {
        return rolesRepository.findByName(roleName).orElseThrow(() -> new RuntimeException("Role not found"));
    }

    @Override
    public Role saveRole(Role role) {
        return rolesRepository.save(role);
    }

    @Override
    public Set<Role> findRolesByNames(Set<String> roleNames) {
        return rolesRepository.findByNameIn(roleNames);
    }

    @Override
    public void deleteRole(Role role) {
        rolesRepository.delete(role);
    }

    @Override
    public Role updateById(Long id, String roleName) {
        Role roleToUpdate = rolesRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
        roleToUpdate.setName(roleName);
        return rolesRepository.save(roleToUpdate);
    }
}
