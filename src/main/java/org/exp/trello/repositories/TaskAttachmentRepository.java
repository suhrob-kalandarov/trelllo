package org.exp.trello.repositories;

import org.exp.trello.models.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskAttachmentRepository extends JpaRepository<Task, Integer> {
}