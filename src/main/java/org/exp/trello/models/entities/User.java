package org.exp.trello.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.exp.trello.models.BaseEntity;
import org.exp.trello.models.enums.UserRole;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trello_users")
public class User extends BaseEntity {

    @NotNull
    @NotBlank(message = "Full name must not be blank")
    private String fullName;

    @NotNull
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email must not be blank")
    private String email;

    @NotNull
    @Size(min = 6, message = "Password should have at least 6 characters")
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<UserRole> roles;
}
