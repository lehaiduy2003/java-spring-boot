package com.example.onlinecourses.mappers;

import com.example.onlinecourses.dtos.models.PermissionDTO;
import com.example.onlinecourses.models.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PermissionMapper {
    PermissionMapper INSTANCE = Mappers.getMapper(PermissionMapper.class);
    PermissionDTO toDTO(Permission permission);
    Permission toEntity(PermissionDTO permissionDTO);
}
