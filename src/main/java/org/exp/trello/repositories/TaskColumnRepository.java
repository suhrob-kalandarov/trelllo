package org.exp.trello.repositories;

import org.exp.trello.models.entities.TaskColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskColumnRepository extends JpaRepository<TaskColumn, Integer> {
    List<TaskColumn> findAllByOrderByPositionAsc();

    @Query("SELECT MAX(c.position) FROM TaskColumn c")
    Integer findMaxPosition();
}
