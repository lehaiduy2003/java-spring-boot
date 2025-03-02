package com.example.onlinecourses.services;
import com.example.onlinecourses.dtos.requests.post.UserCreationDTO;
import com.example.onlinecourses.dtos.models.UserDTO;
import com.example.onlinecourses.dtos.responses.data.UserDataDTO;
import com.example.onlinecourses.exceptions.ResourceAlreadyExistException;
import com.example.onlinecourses.mappers.UserMapper;
import com.example.onlinecourses.models.Role;
import com.example.onlinecourses.models.User;
import com.example.onlinecourses.services.interfaces.IRoleService;
import com.example.onlinecourses.services.interfaces.IUserService;
import com.example.onlinecourses.configs.impls.UserDetailsImpl;
import org.springframework.data.domain.Pageable;
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
        User user = usersRepository.findUserByEmailOrUsername(identifier, identifier)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with: " + identifier));
        return new UserDetailsImpl(
            user.getId(),
            user.getEmail(),
            user.getPhoneNumber(),
            user.getUsername(),
            user.getPassword(),
            true,
            user.getOauthProviders(),
            user.getRoles()
        );
    }

    @Override
    public UserDTO updateById(Long id, UserDTO userDTO) {
        User user = usersRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));

        return update(user, userDTO);
    }


    @Override
    public UserDTO create(UserCreationDTO userCreationDTO) {
        if(isEmailExist(userCreationDTO.getEmail()))
            throw new ResourceAlreadyExistException("User with email " + userCreationDTO.getEmail() + " already exists");
        // query roles by name
        Set<String> roleNames = userCreationDTO.getRoles().stream()
            .map(Role::getName)
            .collect(Collectors.toSet());
        Set<Role> roles = roleService.findRolesByNames(roleNames);
        userCreationDTO.setRoles(roles);
        User user = UserMapper.INSTANCE.toEntity(userCreationDTO);
        usersRepository.save(user);
        return UserMapper.INSTANCE.toDTO(user);
    }

    @Override
    public void deleteByEmail(String email) {
        if(!isEmailExist(email)) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        usersRepository.deleteUserByEmail(email);
    }

    @Override
    public void deleteByUsername(String username) {
        if(!isUsernameExist(username)) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        usersRepository.deleteUserByUsername(username);
    }

    @Override
    public void deleteByPhoneNumber(String phoneNumber) {
        if(!isPhoneNumberExist(phoneNumber)) {
            throw new UsernameNotFoundException("User not found with phone number: " + phoneNumber);
        }
        usersRepository.deleteUserByPhoneNumber(phoneNumber);
    }

    @Override
    public UserDTO updateByEmail(String email, UserDTO userDTO) {
        UserDTO foundUser = findByEmail(email);
        User user = UserMapper.INSTANCE.toEntity(foundUser);

        return update(user, userDTO);
    }

    private UserDTO update(User user, UserDTO userDTO) {

        user.setFullname(userDTO.getFullname());
        user.setAddress(userDTO.getAddress());
        user.setBio(userDTO.getBio());
        user.setAvatar(userDTO.getAvatar());
        user.setDob(userDTO.getDob());
        user.setGender(userDTO.isGender());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setUsername(userDTO.getUsername());

        User updatedUser = usersRepository.save(user);
        return UserMapper.INSTANCE.toDTO(updatedUser);
    }

    @Override
    public UserDTO updateByPhoneNumber(String phoneNumber, UserDTO userDTO) {
        UserDTO foundUser = findByPhoneNumber(phoneNumber);
        User user = UserMapper.INSTANCE.toEntity(foundUser);

        return update(user, userDTO);
    }

    // Throws IllegalArgumentException if the email is not valid format
    @Override
    public UserDTO findByEmail(String email) throws IllegalArgumentException {
        User user = usersRepository.findUserByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return UserMapper.INSTANCE.toDTO(user);
    }

    // Throws IllegalArgumentException if the phone number is not valid format
    @Override
    public UserDTO findByPhoneNumber(String phoneNumber) throws IllegalArgumentException {
        User user = usersRepository.findUserByPhoneNumber(phoneNumber)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with phone number: " + phoneNumber));

        return UserMapper.INSTANCE.toDTO(user);
    }

    // Throws IllegalArgumentException if the username is not valid format
    @Override
    public UserDTO findByUsername(String username) throws IllegalArgumentException {
        User user = usersRepository.findUserByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return UserMapper.INSTANCE.toDTO(user);
    }


    @Override
    public UserDataDTO[] findMany(Pageable pageable) {
        List<User> users = usersRepository.findAll(pageable).getContent();
        return users.stream()
            .map(UserMapper.INSTANCE::toUserDataDTO)
            .toArray(UserDataDTO[]::new);
    }

    @Override
    public UserDTO findById(Long id) {
        User user = usersRepository.findById(id)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
        return UserMapper.INSTANCE.toDTO(user);
    }

    // throws IllegalArgumentException if the email is not valid format
    private boolean isEmailExist(String email)  throws IllegalArgumentException {
        return usersRepository.existsUserByEmail(email);
    }

    // throws IllegalArgumentException if the phone number is not valid format
    private boolean isPhoneNumberExist(String phoneNumber)  throws IllegalArgumentException {
        return usersRepository.existsUserByPhoneNumber(phoneNumber);
    }

    // throws IllegalArgumentException if the username is not valid format
    private boolean isUsernameExist(String username)  throws IllegalArgumentException {
        return usersRepository.existsUserByUsername(username);
    }
}