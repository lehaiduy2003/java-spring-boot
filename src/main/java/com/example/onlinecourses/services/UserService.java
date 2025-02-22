package com.example.onlinecourses.services;


import com.example.onlinecourses.dtos.reqMethod.post.UserCreationDTO;
import com.example.onlinecourses.dtos.models.UserDTO;
import com.example.onlinecourses.mappers.UserMapper;
import com.example.onlinecourses.models.Role;
import com.example.onlinecourses.models.User;
import com.example.onlinecourses.services.Interfaces.IRoleService;
import com.example.onlinecourses.services.Interfaces.IUserService;
import com.example.onlinecourses.specifications.UserDetailsImpl;
import com.example.onlinecourses.specifications.UserSpecification;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.onlinecourses.repositories.UsersRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {

    private final UsersRepository usersRepository;
    private final IRoleService roleService;

    public UserService(UsersRepository usersRepository, IRoleService roleService) {
        this.usersRepository = usersRepository;
        this.roleService = roleService;
    }

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        User user = usersRepository.findByEmailOrUsernameOrPhoneNumber(identifier, identifier, identifier)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with: " + identifier));
        return new UserDetailsImpl(user.getId(), user.getEmail(), user.getPhoneNumber(), user.getUsername(), user.getPassword(), true, user.getRoles());
    }

    @Override
    public UserDTO updateById(Long id, UserDTO userDTO) {
        User user = usersRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));

        return update(user, userDTO);
    }

    @Override
    public UserDTO create(UserCreationDTO userCreationDTO) throws IllegalArgumentException {
        if(isEmailExist(userCreationDTO.getEmail()) || isPhoneNumberExist(userCreationDTO.getPhoneNumber()) || isUsernameExist(userCreationDTO.getUsername())) {
            throw new IllegalArgumentException("User already exists");
        }
        // query roles by name
        Set<String> roleNames = userCreationDTO.getRoles().stream()
            .map(Role::getName)
            .collect(Collectors.toSet());
        Set<Role> roles = roleService.findRolesByNames(roleNames);
        userCreationDTO.setRoles(roles);
        User user = UserMapper.INSTANCE.createUser(userCreationDTO);
        usersRepository.save(user);
        return UserMapper.INSTANCE.toDTO(user);
    }

    @Override
    public Boolean deleteByEmail(String email) throws IllegalArgumentException {
        // Check if user exists
        // Already implemented encryption in findByEmail method
        findByEmail(email);
        usersRepository.delete(UserSpecification.hasEmail(email));
        return true;
    }

    @Override
    public Boolean deleteByPhoneNumber(String phoneNumber) throws IllegalArgumentException {
        // Already implemented encryption in findByEmail method
        findByPhoneNumber(phoneNumber);
        usersRepository.delete(UserSpecification.hasEmail(phoneNumber));
        return true;
    }

    @Override
    public UserDTO updateByEmail(String email, UserDTO userDTO) throws IllegalArgumentException {
        UserDTO foundUser = findByEmail(email);
        User user = UserMapper.INSTANCE.toUser(foundUser);

        return update(user, userDTO);
    }

    private UserDTO update(User user, UserDTO userDTO) {

        user.setFullname(userDTO.getFullname());
        user.setAddress(userDTO.getAddress());
        user.setBio(userDTO.getBio());
        user.setAvatar(userDTO.getAvatar());
        user.setDob(userDTO.getDob());
        user.setGender(userDTO.isGender());

        User updatedUser = usersRepository.save(user);
        return UserMapper.INSTANCE.toDTO(updatedUser);
    }

    @Override
    public UserDTO updateByPhoneNumber(String phoneNumber, UserDTO userDTO) {
        UserDTO foundUser = findByPhoneNumber(phoneNumber);
        User user = UserMapper.INSTANCE.toUser(foundUser);

        return update(user, userDTO);
    }

    @Override
    public UserDTO findByEmail(String email) {
        User user = usersRepository.findOne(UserSpecification.hasEmail(email))
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return UserMapper.INSTANCE.toDTO(user);
    }

    @Override
    public UserDTO findByPhoneNumber(String phoneNumber) {
        User user = usersRepository.findOne(UserSpecification.hasPhoneNumber(phoneNumber))
            .orElseThrow(() -> new UsernameNotFoundException("User not found with phone number: " + phoneNumber));

        return UserMapper.INSTANCE.toDTO(user);
    }

    @Override
    public UserDTO findByUsername(String username) {
        User user = usersRepository.findOne(UserSpecification.hasUsername(username))
            .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return UserMapper.INSTANCE.toDTO(user);
    }

    @Override
    public UserDTO[] findMany(@Nullable Sort sort) {
        List<User> users = usersRepository.findAll(sort);
        return users.stream().map(UserMapper.INSTANCE::toDTO).toArray(UserDTO[]::new);
    }


    private boolean isEmailExist(String email) {
        return usersRepository.exists(UserSpecification.hasEmail(email));
    }

    private boolean isPhoneNumberExist(String phoneNumber) {
        return usersRepository.exists(UserSpecification.hasPhoneNumber(phoneNumber));
    }

    private boolean isUsernameExist(String username) {
        return usersRepository.exists(UserSpecification.hasUsername(username));
    }
}