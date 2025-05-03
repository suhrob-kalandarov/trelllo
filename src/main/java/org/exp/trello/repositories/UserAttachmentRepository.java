package org.exp.trello.repositories;

import org.exp.trello.models.entities.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAttachmentRepository extends JpaRepository<Attachment, Integer> {
}
