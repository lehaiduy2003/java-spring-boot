package com.example.onlinecourses.models;

import com.example.onlinecourses.utils.EncryptData;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    private String password;

    @NotBlank
    @Size(max = 100)
    @Column(unique = true, nullable = false)
    private String username;

    private boolean gender;
    private Date dob;
    private String avatar;
    private String bio;

    @NotBlank
    @Email
    @Size(max = 100)
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    @Size(max = 11)
    @Column(unique = true, name = "phone_number", nullable = false)
    private String phoneNumber;

    @NotBlank
    @Size(max = 120)
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Size(max = 255)
    private String address;

    @Column(name = "created_at")
    private Date createdAt = new Date();

    @Column(name = "updated_at")
    private Date updatedAt = new Date();

    // Mapping with question by creator attribute in Question class
    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY)
    private List<Question> questions;

    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY)
    private List<Exam> exams;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "users_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    // Mapping with discussion by user attribute in Discussion class
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Discussion> discussions;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Return the roles or authorities of the user
        return roles.stream()
            .flatMap(role -> role.getPermissions().stream())
            .map(permission -> new SimpleGrantedAuthority(permission.getName()))
            .collect(Collectors.toList());
    }

    // Encrypt sensitive fields before saving or updating the user data
    @PrePersist
    @PreUpdate
    public void encryptSensitiveFields() {
        this.email = EncryptData.encrypt(this.email);
        this.phoneNumber = EncryptData.encrypt(this.phoneNumber);
        this.address = EncryptData.encrypt(this.address);
    }

    // Decrypt sensitive fields before returning the user data
    @PostLoad
    public void decryptSensitiveFields() {
        this.email = EncryptData.decrypt(this.email);
        this.phoneNumber = EncryptData.decrypt(this.phoneNumber);
        this.address = EncryptData.decrypt(this.address);
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public void addRole(Role role) {
        this.roles.add(role);
        role.getUsers().add(this);
    }
    public void removeRole(Role role) {
        this.roles.remove(role);
        role.getUsers().remove(this);
    }
    public void removeAllRoles() {
        for (Role role : roles) {
            role.getUsers().remove(this);
        }
        roles.clear();
    }

    public void addQuestion(Question question) {
        this.questions.add(question);
    }
    public void updateQuestion(Question question) {
        for (int i = 0; i < questions.size(); i++) {
            if (questions.get(i).getId().equals(question.getId())) {
                questions.set(i, question);
                break;
            }
        }
    }
    public void removeQuestion(Question question) {
        this.questions.remove(question);
    }
    public void removeAllQuestions() {
        this.questions.clear();
    }

    public void addDiscussion(Discussion discussion) {
        this.discussions.add(discussion);
    }
    public void updateDiscussion(Discussion discussion) {
        for (int i = 0; i < discussions.size(); i++) {
            if (discussions.get(i).getId().equals(discussion.getId())) {
                discussions.set(i, discussion);
                break;
            }
        }
    }
    public void removeDiscussion(Discussion discussion) {
        this.discussions.remove(discussion);
    }
    public void removeAllDiscussions() {
        this.discussions.clear();
    }

    public void addExam(Exam exam) {
        this.exams.add(exam);
    }
    public void updateExam(Exam exam) {
        for (int i = 0; i < exams.size(); i++) {
            if (exams.get(i).getId().equals(exam.getId())) {
                exams.set(i, exam);
                break;
            }
        }
    }
    public void removeExam(Exam exam) {
        this.exams.remove(exam);
    }
    public void removeAllExams() {
        this.exams.clear();
    }

    // Getters and Setters
}