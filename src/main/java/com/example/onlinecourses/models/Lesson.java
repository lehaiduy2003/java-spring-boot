package com.example.onlinecourses.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lessons")
@Builder
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chapter_id", nullable = false)
    private Chapter chapter; // For many-to-one relationship with Chapter

    @NotBlank
    @Column(name = "video_url", nullable = false)
    private String videoUrl;

    // For one-to-many relationship with Discussion by lesson attribute
    @OneToMany(mappedBy = "lesson", fetch = FetchType.LAZY)
    private List<Discussion> discussions;

    public void addDiscussion(Discussion discussion) {
        this.discussions.add(discussion);
        discussion.setLesson(this);
    }

    public void removeDiscussion(Discussion discussion) {
        this.discussions.remove(discussion);
        discussion.setLesson(null);
    }

}
