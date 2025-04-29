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

    @Column(name = "in_active")
    private Boolean inActive = false;

    @Column(nullable = false)
    private LocalDateTime deadline = LocalDateTime.now().plusDays(3);

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id", nullable = false)
    private TaskStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(mappedBy = "task", cascade = CascadeType.ALL)
    private TaskAttachment attachment;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<Comment> comments;
}
