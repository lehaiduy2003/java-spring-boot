package com.example.onlinecourses.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "discussions")
@Builder
public class Discussion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson; // For many-to-one relationship with Lesson

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "created_at", nullable = false)
    private Date createdAt = new Date();

    @Column(name = "updated_at", nullable = false)
    private Date updatedAt = new Date();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Discussion parent; // For many-to-one relationship with Discussion (self-referencing)

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Discussion> replies; // For one-to-many relationship with Discussion (self-referencing)

    public void addReply(Discussion reply) {
        this.replies.add(reply);
        reply.setParent(this);
    }

    public void removeReply(Discussion reply) {
        this.replies.remove(reply);
        reply.setParent(null);
    }

}
