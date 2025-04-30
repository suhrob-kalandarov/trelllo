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
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {

            User user = new User();
            user.setUsername("User One");
            user.setEmail("user@gmail.com");
            user.setPassword(passwordEncoder.encode("root123"));
            user.setRoles(Collections.singletonList(UserRole.USER));
            user.setVerified(true);

            User manager = new User();
            manager.setUsername("Manager One");
            manager.setEmail("manager@gmail.com");
            manager.setPassword(passwordEncoder.encode("root123"));
            manager.setRoles(Collections.singletonList(UserRole.MANAGER));
            manager.setVerified(true);

            User admin = new User();
            admin.setUsername("Admin One");
            admin.setEmail("admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("root123"));
            admin.setRoles(Collections.singletonList(UserRole.ADMIN));
            admin.setVerified(true);

            userRepository.saveAll(List.of(user, manager, admin));

            System.out.println("✅ 3ta User muvaffaqiyatli saqlandi!");
        }
    }
}
