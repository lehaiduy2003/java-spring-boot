package com.example.onlinecourses.mappers;


import com.example.onlinecourses.dtos.requests.post.UserCreationDTO;
import com.example.onlinecourses.dtos.models.UserDTO;
import com.example.onlinecourses.dtos.responses.data.UserDataDTO;
import com.example.onlinecourses.models.User;
import com.example.onlinecourses.configs.impls.UserDetailsImpl;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserDTO toDTO(User user);
    User toEntity(UserDTO userDTO);
    User toEntity(UserCreationDTO userCreationDTO);
    User toEntity(UserDetailsImpl userDetails);
    UserDataDTO toUserDataDTO(User user);
    UserDataDTO toUserDataDTO(UserDTO userDTO);
    UserDataDTO toUserDataDTO(UserDetailsImpl userDetails);
}