package com.example.onlinecourses.services.Interfaces;


import com.example.onlinecourses.dtos.UserDTO;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDTO create(UserDTO userDTO);
    Boolean deleteByEmail(String email);
    Boolean deleteByPhoneNumber(String phoneNumber);
    UserDTO updateByEmail(String email, UserDTO userDTO);
    UserDTO updateByPhoneNumber(String phoneNumber, UserDTO userDTO);
    UserDTO findByEmail(String email);
    UserDTO findByPhoneNumber(String phoneNumber);
    UserDTO[] findMany(@Nullable Sort sort);
}
