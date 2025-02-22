package com.example.onlinecourses.services.Interfaces;

import com.example.onlinecourses.dtos.reqMethod.post.UserCreationDTO;
import com.example.onlinecourses.dtos.models.UserDTO;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {
    UserDTO updateById(Long id, UserDTO userDTO);
    UserDTO create(UserCreationDTO userCreationDTO);
    Boolean deleteByEmail(String email);
    Boolean deleteByPhoneNumber(String phoneNumber);
    UserDTO updateByEmail(String email, UserDTO userDTO);
    UserDTO updateByPhoneNumber(String phoneNumber, UserDTO userDTO);
    UserDTO findByEmail(String email);
    UserDTO findByPhoneNumber(String phoneNumber);
    UserDTO findByUsername(String username);
    UserDTO[] findMany(@Nullable Sort sort);
}
