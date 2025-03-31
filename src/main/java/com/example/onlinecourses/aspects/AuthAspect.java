package com.example.onlinecourses.aspects;
import com.example.onlinecourses.annotations.AuthorizePermission;
import com.example.onlinecourses.dtos.models.UserDTO;
import com.example.onlinecourses.exceptions.IllegalArgumentException;
import com.example.onlinecourses.exceptions.PermissionDeniedException;
import com.example.onlinecourses.mappers.RoleMapper;
import com.example.onlinecourses.models.Role;
import com.example.onlinecourses.services.interfaces.IRoleService;
import com.example.onlinecourses.services.interfaces.IUserService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Aspect
@Component
public class AuthAspect {
    private static final Logger LOGGER = Logger.getLogger(AuthAspect.class.getName());
    private final IUserService userService;
    private final IRoleService roleService;

    public AuthAspect(IUserService userService, IRoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Before("@annotation(com.example.onlinecourses.annotations.AuthorizePermission)")
    public void authorizePermission(JoinPoint joinPoint) {
        // Get permission from annotation on target class
        LOGGER.info("Checking permission");
        String className = joinPoint.getTarget().getClass().getName(); // get class name
        if(LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info(String.format("Class: %s", className));
        }
        // Get method name
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        if(LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info(String.format("Method: %s", method.getName()));
        }
        // log method arguments
        Object[] args = joinPoint.getArgs();
        if(LOGGER.isLoggable(Level.INFO)) {
            Arrays.stream(args).forEach(arg -> LOGGER.info(String.format("Method arguments: %s", arg)));
        }
        // Get permission from annotation
        AuthorizePermission authorizePermission = method.getAnnotation(AuthorizePermission.class);
        String permission = authorizePermission.value();
        if(LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info(String.format("Permission: %s", permission));
        }
        // if permission is empty from annotation, throw IllegalArgumentException
        if(permission.isEmpty()) {
            throw new IllegalArgumentException("Permission cannot be empty");
        }
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info(String.format("Checking permission: %s", permission));
        }
        // Check if user has permission
        // if not throw new PermissionDeniedException("Permission denied")
        long userId = (Long) joinPoint.getArgs()[0]; // get userId from method arguments
        UserDTO userDTO = userService.findById(userId);

        // Map roleDTO to role entity
        Set<Role> roles = RoleMapper.INSTANCE.toEntities(userDTO.getRoles());

        // if user does not have permission, throw PermissionDeniedException
        if(!roleService.isRolePermitted(roles, permission)) {
            throw new PermissionDeniedException("Permission denied");
        }

        // Log permission granted and user details
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info(String.format("Permission granted: %s", permission));
            LOGGER.info(String.format("UserId: %s", userId));
            LOGGER.info(String.format("User data: %s", userDTO));
        }
    }
}
