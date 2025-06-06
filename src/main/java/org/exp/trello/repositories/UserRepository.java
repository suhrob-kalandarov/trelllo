package org.exp.trello.repositories;

import org.exp.trello.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findAllByActiveTrue();
    List<User> findAllByActiveFalse();
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
