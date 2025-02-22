package com.example.onlinecourses.services.Interfaces;

import com.example.onlinecourses.models.Role;

import java.util.Set;

public interface IRoleService {
    Set<Role> findRolesByNames(Set<String> roleNames);
    Role findRoleByName(String roleName);
    Role saveRole(Role role);
    void deleteRole(Role role);
    Role updateById(Long id, String roleName);
}
