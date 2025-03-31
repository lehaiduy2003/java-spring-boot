package com.example.onlinecourses.models;

import com.example.onlinecourses.enums.QuestionLevel;
import com.example.onlinecourses.enums.QuestionType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "questions")
@Builder
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "question_text", nullable = false)
    private String questionText;

    private String answer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chapter_id", nullable = false)
    private Chapter chapter;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type")
    private QuestionType questionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_level")
    private QuestionLevel questionLevel;

    // Mapping for one-to-many relationship with QuestionHint by question attribute
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<QuestionHint> hints;

    @NotBlank
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator; // For many-to-one relationship with User

    @Column(name = "created_at")
    private Date createdAt = new Date();

    @Column(name = "updated_at")
    private Date updatedAt = new Date();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam; // For many-to-one relationship with Exam

    public void addQuestionHint(QuestionHint hint) {
        hints.add(hint);
    }

    public void removeQuestionHint(QuestionHint hint) {
        hints.remove(hint);
    }

    public void removeAllQuestionHints() {
        hints.clear();
    }
}