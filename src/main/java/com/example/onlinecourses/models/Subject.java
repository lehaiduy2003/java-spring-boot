package com.example.onlinecourses.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "subjects")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    private Set<Course> courses;

    // Mapping with Exam by subject attribute in Exam class
    @NotBlank
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject", nullable = false)
    private List<Exam> exams;

    public void addCourse(Course course) {
        this.courses.add(course);
        course.setSubject(this);
    }

    public void removeCourse(Course course) {
        this.courses.remove(course);
        course.setSubject(null);
    }

    public void addExam(Exam exam) {
        this.exams.add(exam);
        exam.setSubject(this);
    }

    public void removeExam(Exam exam) {
        this.exams.remove(exam);
        exam.setSubject(null);
    }
}