package com.example.onlinecourses.models;

import com.example.onlinecourses.enums.CourseLevel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "courses")
@Builder
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    private String image;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject; // For one-to-one relationship with Subject

    @Enumerated(EnumType.STRING)
    private CourseLevel courseLevel;

    @NotBlank
    private String prerequisite = "No prerequisite";

    // For one-to-many relationship with Chapter by course attribute
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Chapter> chapters;

    @Column(name = "created_at")
    private Date createdAt = new Date();

    @Column(name = "updated_at")
    private Date updatedAt = new Date();

    public void addChapter(Chapter chapter) {
        this.chapters.add(chapter);
        chapter.setCourse(this);
    }

    public void removeChapter(Chapter chapter) {
        this.chapters.remove(chapter);
        chapter.setCourse(null);
    }
}
