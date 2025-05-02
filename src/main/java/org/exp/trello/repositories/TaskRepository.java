package org.exp.trello.repositories;

import org.exp.trello.models.entities.Task;
import org.exp.trello.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findAllByActiveTrue();

    @Query("SELECT COUNT(t) FROM Task t WHERE t.column.id = :columnId")
    Long countByColumnId(@Param("columnId") Integer columnId);

    List<Task> findAllByColumnId(Integer columnId);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.user.id = :userId")
    Integer findCountByUserId(Integer userId);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.user.id = :userId and t.active=true")
    Integer findCountByUserIdAndActiveTrue(Integer userId);

    List<Task> streamTasksByUser(User user);

    List<Task> findAllByUser(User user);
}
