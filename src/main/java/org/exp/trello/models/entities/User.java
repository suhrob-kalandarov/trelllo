package org.exp.trello.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.exp.trello.models.BaseEntity;
import org.exp.trello.models.enums.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "trello_users")
public class User extends BaseEntity implements UserDetails {

    @NotNull
    @NotBlank(message = "Username must not be blank")
    private String username;

    @NotNull
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email must not be blank")
    private String email;

    @NotNull
    @Size(min = 6, message = "Password should have at least 6 characters")
    private String password;

    @OneToOne
    @JoinColumn(name = "attachment_id")
    private Attachment attachment;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<UserRole> roles;


    @Transient
    @Size(min = 6, message = "Not matches")
    private String confirmPassword;

    @Transient
    private boolean verified = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> (GrantedAuthority) () -> "ROLE_" + role.name())
                .toList();
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getName(){
        return this.username;
    }

    public void setName(String name){
        this.username = name;
    }
}
