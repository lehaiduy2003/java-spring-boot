package com.example.onlinecourses.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Set;


@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "users")
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String password;

    @Size(max = 100)
    @Column(unique = true)
    private String username;

    private boolean gender;
    private Date dob;
    private String avatar;
    private String bio;

    @NotBlank
    @Email(message = "Email should be valid")
    @Size(max = 100)
    @Column(unique = true, nullable = false)
    private String email;

    @Size(max = 11)
    @Column(unique = true, name = "phone_number")
    private String phoneNumber;

    @NotBlank
    @Size(max = 120)
    @Column(name = "fullname", nullable = false)
    private String fullname;

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

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<OauthProvider> oauthProviders;

    // Encrypt sensitive fields before saving or after updating the user data
    @PrePersist
    @PreUpdate
    public void preSave() {
        if(this.createdAt == null)
            this.createdAt = new Date();

        this.updatedAt = new Date();
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


    public void addOauthProvider(OauthProvider oauthProvider) {
        this.oauthProviders.add(oauthProvider);
        oauthProvider.setUser(this);
    }

    public void removeOauthProvider(OauthProvider oauthProvider) {
        this.oauthProviders.remove(oauthProvider);
        oauthProvider.setUser(null);
    }

}