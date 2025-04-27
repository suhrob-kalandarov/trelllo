package org.exp.trello.repositories;

import org.exp.trello.models.entities.UserAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAttachmentRepository extends JpaRepository<UserAttachment, Integer> {
}
