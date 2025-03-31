package com.example.onlinecourses.mappers;

import com.example.onlinecourses.dtos.models.RoleDTO;
import com.example.onlinecourses.models.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);
    RoleDTO toDTO(Role role);
    Set<RoleDTO> toDTOs(Set<Role> roles);
    Role toEntity(RoleDTO roleDTO);
    Set<Role> toEntities(Set<RoleDTO> roleDTOs);
}
