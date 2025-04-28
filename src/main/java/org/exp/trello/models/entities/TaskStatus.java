package org.exp.trello.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.exp.trello.models.BaseEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "task_statuses")
public class TaskStatus extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(name = "in_active")
    private Boolean inActive = false;
}
