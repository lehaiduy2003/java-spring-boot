package com.example.onlinecourses.services.Interfaces;

import com.example.onlinecourses.dtos.PermissionDTO;

import java.util.List;
import java.util.Optional;

public interface PermissionService {
    Optional<List<PermissionDTO>> getPermissionsByEmail(String email);
    Optional<List<PermissionDTO>> getPermissionsByPhoneNumber(String phoneNumber);
    Optional<List<PermissionDTO>> getPermissionsByUserId(Long id);
    Optional<PermissionDTO> getPermissionByEmail(String email);
    Optional<PermissionDTO> getPermissionByPhoneNumber(String phoneNumber);
    Optional<PermissionDTO> getPermissionByUserId(Long id);
}
