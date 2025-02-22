package com.example.onlinecourses.models;

import com.example.onlinecourses.enums.ExamType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exams")
@Builder
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "exam_type")
    private ExamType examType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject; // For many-to-one relationship with Subject

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator; // For many-to-one relationship with User

    // Mapping with Question by exam attribute in Question class
    @OneToMany(mappedBy = "exam", fetch = FetchType.LAZY)
    private List<Question> questions;

    public void addQuestion(Question question) {
        this.questions.add(question);
        question.setExam(this);
    }

    public void removeQuestion(Question question) {
        this.questions.remove(question);
        question.setExam(null);
    }

}
