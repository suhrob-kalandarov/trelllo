package org.exp.trello.configurations;

import lombok.RequiredArgsConstructor;
import org.exp.trello.models.entities.User;
import org.exp.trello.models.enums.UserRole;
import org.exp.trello.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User user1 = User.builder()
                    .username("User One")
                    .email("user@gmail.com")
                    .password(passwordEncoder.encode("root123"))
                    .roles(Collections.singletonList(UserRole.USER))
                    .build();

            User manager = User.builder()
                    .username("Manager One")
                    .email("manager@gmail.com")
                    .password(passwordEncoder.encode("root123"))
                    .roles(Collections.singletonList(UserRole.MANAGER))
                    .build();

            User admin = User.builder()
                    .username("Admin One")
                    .email("admin@gmail.com")
                    .password(passwordEncoder.encode("root123"))
                    .roles(Collections.singletonList(UserRole.ADMIN))
                    .build();

            userRepository.saveAll(List.of(user1, manager, admin));

            System.out.println("✅ 3ta User muvaffaqiyatli saqlandi!");
        }
    }
}
