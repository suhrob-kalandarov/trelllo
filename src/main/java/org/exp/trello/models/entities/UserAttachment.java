package org.exp.trello.models.entities;

import jakarta.persistence.*;
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
@Table(name = "users_attachments")
public class UserAttachment extends BaseEntity {

    private byte[] content;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}