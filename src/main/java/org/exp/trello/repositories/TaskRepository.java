package org.exp.trello.repositories;

import org.exp.trello.models.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findAllByInActiveTrue();
}
