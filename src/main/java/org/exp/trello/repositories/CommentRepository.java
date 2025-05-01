package org.exp.trello.repositories;

import org.exp.trello.models.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query("SELECT COUNT(t) FROM Comment t WHERE t.user.id = :userId")
    Integer findCountByUserId(Integer userId);
}
