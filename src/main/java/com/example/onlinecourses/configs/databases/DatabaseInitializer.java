package com.example.onlinecourses.configs.databases;

import com.example.onlinecourses.models.Permission;
import com.example.onlinecourses.models.Role;
import com.example.onlinecourses.models.User;
import com.example.onlinecourses.repositories.PermissionsRepository;
import com.example.onlinecourses.repositories.RolesRepository;
import com.example.onlinecourses.repositories.UsersRepository;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DatabaseInitializer {
    private final UsersRepository usersRepository;
    private final RolesRepository rolesRepository;
    private final PermissionsRepository permissionsRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    @Transactional
    public void init() {
        if (rolesRepository.count() == 0 && permissionsRepository.count() == 0 && usersRepository.count() == 0) {
            // Create permissions
            Permission readUserPermission = Permission.builder()
                .name("READ_USER")
                .description("Allows reading user details")
                .build();
            Permission manageUserPermission = Permission.builder()
                .name("MANAGE_USER")
                .description("Allows creating, updating, and deleting users")
                .build();

            Permission readRolePermission = Permission.builder()
                .name("READ_ROLE")
                .description("Allows reading role details")
                .build();
            Permission manageRolePermission = Permission.builder()
                .name("MANAGE_ROLE")
                .description("Allows creating, updating, and deleting roles")
                .build();
            Permission readPermissionPermission = Permission.builder()
                .name("READ_PERMISSION")
                .description("Allows reading permission details")
                .build();
            Permission managePermissionPermission = Permission.builder()
                .name("MANAGE_PERMISSION")
                .description("Allows creating, updating, and deleting permissions")
                .build();
            Permission readCoursePermission = Permission.builder()
                .name("READ_COURSE")
                .description("Allows reading course details")
                .build();
            Permission manageCoursePermission = Permission.builder()
                .name("MANAGE_COURSE")
                .description("Allows creating, updating, and deleting courses")
                .build();
            Permission readChapterPermission = Permission.builder()
                .name("READ_CHAPTER")
                .description("Allows reading chapter details")
                .build();
            Permission manageChapterPermission = Permission.builder()
                .name("MANAGE_CHAPTER")
                .description("Allows creating, updating, and deleting chapters")
                .build();
            Permission readLessonPermission = Permission.builder()
                .name("READ_LESSON")
                .description("Allows reading lesson details")
                .build();
            Permission manageLessonPermission = Permission.builder()
                .name("MANAGE_LESSON")
                .description("Allows creating, updating, and deleting lessons")
                .build();
            Permission readExamPermission = Permission.builder()
                .name("READ_EXAM")
                .description("Allows reading exam details")
                .build();
            Permission manageExamPermission = Permission.builder()
                .name("MANAGE_EXAM")
                .description("Allows creating, updating, and deleting exams")
                .build();
            Permission readQuestionPermission = Permission.builder()
                .name("READ_QUESTION")
                .description("Allows reading question details")
                .build();
            Permission manageQuestionPermission = Permission.builder()
                .name("MANAGE_QUESTION")
                .description("Allows creating, updating, and deleting questions")
                .build();
            Permission readDiscussionPermission = Permission.builder()
                .name("READ_DISCUSSION")
                .description("Allows reading discussion details")
                .build();
            Permission manageDiscussionPermission = Permission.builder()
                .name("MANAGE_DISCUSSION")
                .description("Allows creating, updating, and deleting discussions")
                .build();
            Permission readGradeReportPermission = Permission.builder()
                .name("READ_GRADE_REPORT")
                .description("Allows reading grade reports")
                .build();
            Permission manageGradeReportPermission = Permission.builder()
                .name("MANAGE_GRADE_REPORT")
                .description("Allows creating, updating, and deleting grade reports")
                .build();

            permissionsRepository.saveAll(Set.of(
                readUserPermission,
                manageUserPermission,
                readRolePermission,
                manageRolePermission,
                readPermissionPermission,
                managePermissionPermission,
                readCoursePermission,
                manageCoursePermission,
                readChapterPermission,
                manageChapterPermission,
                readLessonPermission,
                manageLessonPermission,
                readExamPermission,
                manageExamPermission,
                readQuestionPermission,
                manageQuestionPermission,
                readDiscussionPermission,
                manageDiscussionPermission,
                readGradeReportPermission,
                manageGradeReportPermission
            ));

            // Create roles
            Role adminRole = Role.builder()
                .name("ADMIN")
                .permissions(Set.of(
                    manageUserPermission,
                    manageRolePermission,
                    managePermissionPermission,
                    manageDiscussionPermission
                ))
                .build();
            Role studentRole = Role.builder()
                .name("STUDENT")
                .permissions(Set.of(
                    readCoursePermission,
                    readChapterPermission,
                    readLessonPermission,
                    readExamPermission,
                    readQuestionPermission,
                    readGradeReportPermission,
                    manageDiscussionPermission
                ))
                .build();

            Role teacherRole = Role.builder()
                .name("TEACHER")
                .permissions(Set.of(
                    manageCoursePermission,
                    manageChapterPermission,
                    manageLessonPermission,
                    manageExamPermission,
                    manageQuestionPermission,
                    manageDiscussionPermission,
                    manageGradeReportPermission
                ))
                .build();

            Role observerRole = Role.builder()
                .name("OBSERVER")
                .permissions(Set.of(
                    readCoursePermission,
                    readChapterPermission,
                    readLessonPermission,
                    readGradeReportPermission,
                    manageDiscussionPermission
                ))
                .build();

            Role teacherSimulatorRole = Role.builder()
                .name("TEACHER_SIMULATOR")
                .permissions(Set.of(
                    manageCoursePermission,
                    manageChapterPermission,
                    manageLessonPermission,
                    manageExamPermission,
                    manageQuestionPermission,
                    manageDiscussionPermission,
                    manageGradeReportPermission
                ))
                .build();

            Role studentSimulatorRole = Role.builder()
                .name("STUDENT_SIMULATOR")
                .permissions(Set.of(
                    readCoursePermission,
                    readChapterPermission,
                    readLessonPermission,
                    readExamPermission,
                    readQuestionPermission,
                    readGradeReportPermission,
                    manageDiscussionPermission
                ))
                .build();

            Role observerSimulatorRole = Role.builder()
                .name("OBSERVER_SIMULATOR")
                .permissions(Set.of(
                    readCoursePermission,
                    readChapterPermission,
                    readLessonPermission,
                    readGradeReportPermission,
                    manageDiscussionPermission
                ))
                .build();

            rolesRepository.saveAll(Set.of(
                adminRole,
                studentRole,
                teacherRole,
                observerRole,
                teacherSimulatorRole,
                studentSimulatorRole,
                observerSimulatorRole
            ));

            // Create default admin user
            User adminUser = User.builder()
                .email(Dotenv.load().get("ADMIN_EMAIL"))
                .username(Dotenv.load().get("ADMIN_USERNAME"))
                .password(passwordEncoder.encode(Dotenv.load().get("ADMIN_PASSWORD")))
                .fullname(Dotenv.load().get("ADMIN_FULLNAME"))
                .roles(Set.of(adminRole, teacherSimulatorRole, studentSimulatorRole, observerSimulatorRole))
                .build();


            usersRepository.save(adminUser);
        }
    }

}
