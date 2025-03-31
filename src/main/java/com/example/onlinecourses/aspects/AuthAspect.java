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
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Aspect
@Component
public class AuthAspect extends BaseAspect {
    private final IUserService userService;
    private final IRoleService roleService;

    public AuthAspect(IUserService userService, IRoleService roleService) {
        super(Logger.getLogger(AuthAspect.class.getName()));
        this.userService = userService;
        this.roleService = roleService;
    }

    // Pointcut for methods annotated with @AuthorizePermission
    @Pointcut("@annotation(com.example.onlinecourses.annotations.AuthorizePermission)")
    public void authorize() {}

    @Before("authorize()")
    public void beforeAuthorize(JoinPoint joinPoint) {
        // Extract class name, method name, and permission
        super.logInformation(joinPoint);
        String permission = getPermission(joinPoint);
        // Extract user id from method arguments
        long userId = (Long) joinPoint.getArgs()[0];
        UserDTO userDTO = userService.findById(userId);
        // Map roleDTO to role entity
        Set<Role> roles = RoleMapper.INSTANCE.toEntities(userDTO.getRoles());
        // if user does not have permission, throw PermissionDeniedException
        if(!roleService.isRolePermitted(roles, permission)) {
            throw new PermissionDeniedException("403 - Forbidden: Permission denied");
        }
        String message = String.format("User id = %s permission granted", userDTO.getId());
        super.logMsg(message, Level.INFO);
    }

    @AfterThrowing(pointcut = "authorize()", throwing = "ex")
    public void handleAuthorizationFailed(JoinPoint joinPoint, Exception ex) {
        super.logEx(joinPoint, ex);
    }

    private String getPermission(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        AuthorizePermission annotation = method.getAnnotation(AuthorizePermission.class);
        if (annotation == null) {
            throw new IllegalArgumentException("Method does not have @AuthorizePermission annotation");
        }
        return annotation.value();
    }
}
