package com.example.onlinecourses.services.Interfaces;

import com.example.onlinecourses.dtos.RoleDTO;

public interface RoleService {
    RoleDTO[] getRolesByUserId(Long id);
    RoleDTO[] getRolesByEmail(String email);
    RoleDTO[] getRolesByPhoneNumber(String phoneNumber);
    RoleDTO getRoleByUserId(Long id);
    RoleDTO getRoleByEmail(String email);
    RoleDTO getRoleByPhoneNumber(String phoneNumber);
}
