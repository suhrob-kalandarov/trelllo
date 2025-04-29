package org.exp.trello.models.entities;

import jakarta.persistence.*;
import lombok.*;
import org.exp.trello.models.BaseEntity;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tasks_attachments")
public class TaskAttachment extends BaseEntity {

    private byte[] content;

    @OneToOne
    @JoinColumn(name = "task_id")
    private Task task;
}
