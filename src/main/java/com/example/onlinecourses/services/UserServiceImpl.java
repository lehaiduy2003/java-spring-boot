package com.example.onlinecourses.services;


import com.example.onlinecourses.dtos.UserDTO;
import com.example.onlinecourses.mappers.UserMapper;
import com.example.onlinecourses.models.User;
import com.example.onlinecourses.services.Interfaces.UserService;
import com.example.onlinecourses.utils.EncryptData;
import com.example.onlinecourses.utils.HashUtil;
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
        User user = UserMapper.INSTANCE.toUser(userDTO);
        secureSensitiveFields(user);
        updateRoles(user, userDTO);
        usersRepository.save(user);
        return UserMapper.INSTANCE.toDTO(user);
    }

    @Override
    public Boolean deleteByEmail(String email) throws IllegalArgumentException {
        // Check if user exists
        // Already implemented encryption in findByEmail method
        findByEmail(email);
        String encryptedEmail = EncryptData.encrypt(email);
        usersRepository.deleteByEmail(encryptedEmail);
        return true;
    }

    @Override
    public Boolean deleteByPhoneNumber(String phoneNumber) throws IllegalArgumentException {
        // Already implemented encryption in findByEmail method
        findByPhoneNumber(phoneNumber);
        String encryptedPhoneNumber = EncryptData.encrypt(phoneNumber);
        usersRepository.deleteByPhoneNumber(encryptedPhoneNumber);
        return true;
    }

    @Override
    public UserDTO updateByEmail(String email, UserDTO userDTO) throws IllegalArgumentException {
        UserDTO foundUser = findByEmail(email);
        User user = UserMapper.INSTANCE.toUser(foundUser);

        return update(user, userDTO);
    }

    private void updateRoles(User user, UserDTO userDTO) {
        user.setRoles(userDTO.getRoles());
    }

    private UserDTO update(User user, UserDTO userDTO) {

        user.setFullName(userDTO.getFullName());
        user.setAddress(EncryptData.encrypt(userDTO.getAddress()));
        user.setBio(userDTO.getBio());
        user.setAvatar(userDTO.getAvatar());
        user.setDob(userDTO.getDob());
        user.setGender(userDTO.isGender());

        User updatedUser = usersRepository.save(user);
        return UserMapper.INSTANCE.toDTO(updatedUser);
    }

    private void secureSensitiveFields(User user) {
        user.setPhoneNumber(EncryptData.encrypt(user.getPhoneNumber()));
        user.setAddress(EncryptData.encrypt(user.getAddress()));
        user.setEmail(EncryptData.encrypt(user.getEmail()));
        user.setPassword(HashUtil.hashString(user.getPassword()));
    }

    @Override
    public UserDTO updateByPhoneNumber(String phoneNumber, UserDTO userDTO) {
        UserDTO foundUser = findByPhoneNumber(phoneNumber);
        User user = UserMapper.INSTANCE.toUser(foundUser);

        return update(user, userDTO);
    }

    @Override
    public UserDTO findByEmail(String email) {
        String encryptedEmail = EncryptData.encrypt(email);
        User user = usersRepository.findByEmail(encryptedEmail)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return UserMapper.INSTANCE.toDTO(user);
    }

    @Override
    public UserDTO findByPhoneNumber(String phoneNumber) {
        String encryptedPhoneNumber = EncryptData.encrypt(phoneNumber);
        User user = usersRepository.findByPhoneNumber(encryptedPhoneNumber)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with phone number: " + phoneNumber));

        return UserMapper.INSTANCE.toDTO(user);
    }

    @Override
    public UserDTO[] findMany(@Nullable Sort sort) {
        List<User> users = usersRepository.findAll(sort);

        return users.stream().map(UserMapper.INSTANCE::toDTO).toArray(UserDTO[]::new);

    }
}