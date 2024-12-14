package com.example.onlinecourses.services;


import com.example.onlinecourses.dtos.UserDTO;
import com.example.onlinecourses.mappers.UserMapper;
import com.example.onlinecourses.models.User;
import com.example.onlinecourses.services.Interfaces.UserService;
import com.example.onlinecourses.utils.UtilHelper;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.onlinecourses.repositories.UsersRepository;

import java.util.List;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UsersRepository usersRepository;

    public UserServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        return usersRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    @Override
    public UserDTO create(UserDTO userDTO) throws IllegalArgumentException {
        User user = UserMapper.INSTANCE.userDTOToUser(userDTO);
        hashSensitiveFields(user);
        updateRoles(user, userDTO);
        usersRepository.save(user);
        return UserMapper.INSTANCE.userToUserDTO(user);
    }

    @Override
    public Boolean deleteByEmail(String email) throws IllegalArgumentException {
        String hashedEmail = UtilHelper.hashString(email);
        usersRepository.findByEmail(hashedEmail)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        usersRepository.deleteByEmail(hashedEmail);
        return true;
    }

    @Override
    public Boolean deleteByPhoneNumber(String phoneNumber) throws IllegalArgumentException {
        String hashedPhoneNumber = UtilHelper.hashString(phoneNumber);
        usersRepository.findByPhoneNumber(hashedPhoneNumber)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with phone number: " + phoneNumber));

        usersRepository.deleteByPhoneNumber(hashedPhoneNumber);
        return true;
    }

    @Override
    public UserDTO updateByEmail(String email, UserDTO userDTO) throws IllegalArgumentException {
        String hashedEmail = UtilHelper.hashString(email);
        com.example.onlinecourses.models.User user = usersRepository.findByEmail(hashedEmail)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return update(user, userDTO);
    }

    private void updateRoles(User user, UserDTO userDTO) {
        user.setRoles(userDTO.getRoles());
    }

    private UserDTO update(User user, UserDTO userDTO) {
        user.setFullName(userDTO.getFullName());
        user.setAddress(userDTO.getAddress());
        user.setBio(userDTO.getBio());
        user.setAvatar(userDTO.getAvatar());
        user.setDob(userDTO.getDob());
        user.setGender(userDTO.isGender());

        User updatedUser = usersRepository.save(user);
        return UserMapper.INSTANCE.userToUserDTO(updatedUser);
    }

    private void hashSensitiveFields(User user) {
        user.setPhoneNumber(UtilHelper.hashString(user.getPhoneNumber()));
        user.setAddress(UtilHelper.hashString(user.getAddress()));
        user.setEmail(UtilHelper.hashString(user.getEmail()));
        user.setPassword(UtilHelper.hashString(user.getPassword()));
    }

    @Override
    public UserDTO updateByPhoneNumber(String phoneNumber, UserDTO userDTO) {
        String hashedPhoneNumber = UtilHelper.hashString(phoneNumber);
        User user = usersRepository.findByPhoneNumber(hashedPhoneNumber)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with phone number: " + phoneNumber));

        return update(user, userDTO);
    }

    @Override
    public UserDTO findByEmail(String email) {
        String hashedEmail = UtilHelper.hashString(email);
        User user = usersRepository.findByEmail(hashedEmail)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return UserMapper.INSTANCE.userToUserDTO(user);
    }

    @Override
    public UserDTO findByPhoneNumber(String phoneNumber) {
        String hashedPhoneNumber = UtilHelper.hashString(phoneNumber);
        User user = usersRepository.findByPhoneNumber(hashedPhoneNumber)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with phone number: " + phoneNumber));

        return UserMapper.INSTANCE.userToUserDTO(user);
    }

    @Override
    public UserDTO[] findMany(@Nullable Sort sort) {
        List<User> users = usersRepository.findAll(sort);

        return users.stream().map(UserMapper.INSTANCE::userToUserDTO).toArray(UserDTO[]::new);

    }
}