package org.exp.trello.controllers.auth;


import jakarta.servlet.http.HttpSession;
import org.exp.trello.models.entities.User;
import org.exp.trello.models.enums.UserRole;
import org.exp.trello.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class RegisterController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String registerFrom(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

@PostMapping("/register")
    public String register(@ModelAttribute User user, Model model, HttpSession session) {
        if (userRepository.existsByFullName(user.getFullName())) {
            model.addAttribute("error", "This username is already taken");
            return "register";
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            model.addAttribute("error", "This email is already taken");
            return "register";
        }

        if (!user.getPassword().equals(user.getRepeatPassword())) {
            model.addAttribute("error", "Passwords do not match");
            return "register";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of(UserRole.USER));
        session.setAttribute("user", user);
        return "redirect:/send-cod";
    }

}