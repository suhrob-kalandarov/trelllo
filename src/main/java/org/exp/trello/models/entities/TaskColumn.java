package org.exp.trello.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.exp.trello.models.BaseEntity;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "task_columns")
public class TaskColumn extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(name = "position_number")
    private Integer position;
}
