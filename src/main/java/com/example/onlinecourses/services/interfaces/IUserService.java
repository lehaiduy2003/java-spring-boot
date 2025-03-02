package com.example.onlinecourses.services.interfaces;

import com.example.onlinecourses.dtos.requests.post.UserCreationDTO;
import com.example.onlinecourses.dtos.models.UserDTO;
import com.example.onlinecourses.dtos.responses.data.UserDataDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {
    UserDTO updateById(Long id, UserDTO userDTO);
    UserDTO create(UserCreationDTO userCreationDTO);
    void deleteByEmail(String email);
    void deleteByPhoneNumber(String phoneNumber);
    void deleteByUsername(String username);
    UserDTO updateByEmail(String email, UserDTO userDTO);
    UserDTO updateByPhoneNumber(String phoneNumber, UserDTO userDTO);
    UserDTO findByEmail(String email);
    UserDTO findByPhoneNumber(String phoneNumber);
    UserDTO findById(Long id);
    UserDTO findByUsername(String username);
    UserDataDTO[] findMany(Pageable pageable);
}
