package org.exp.trello.models.entities;

import jakarta.persistence.*;
import lombok.*;
import org.exp.trello.models.BaseEntity;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tasks")
public class Task extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToOne
    @JoinColumn(name = "attachment_id")
    private Attachment attachment;

    @Column(nullable = false)
    private LocalDateTime deadline = LocalDateTime.now().plusDays(3);

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "column_id", nullable = false)
    private TaskColumn column;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany
    @JoinColumn(name = "task_id")
    private List<Comment> comments;
}
