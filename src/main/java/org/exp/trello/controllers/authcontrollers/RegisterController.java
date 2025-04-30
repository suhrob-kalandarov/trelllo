package org.exp.trello.controllers.authcontrollers;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.exp.trello.models.entities.User;
import org.exp.trello.models.enums.UserRole;
import org.exp.trello.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(
            @ModelAttribute User user,
            Model model,
            HttpSession session) {

        if (userRepository.existsByEmail(user.getEmail())) {
            model.addAttribute("error", "This email is already taken");
            return "auth/login";
        }

        if (!user.getPassword().equals(user.getConfirmPassword())) {
            model.addAttribute("error", "Passwords do not match!");
            return "auth/register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of(UserRole.USER));
        session.setAttribute("user", user);
        return "redirect:/send-code";
    }
}
