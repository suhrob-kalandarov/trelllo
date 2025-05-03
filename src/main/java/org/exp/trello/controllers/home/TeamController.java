package org.exp.trello.controllers.home;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.exp.trello.models.entities.User;
import org.exp.trello.repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamController {

    private final UserRepository userRepository;

    /**
     * Display the team management page with all users
     */
    @GetMapping
    public String teamPage(Model model, HttpSession httpSession) {
        List<User> users = userRepository.findAllByActiveTrue();
        model.addAttribute("users", users);
        model.addAttribute("currentUser", httpSession.getAttribute("user"));
        return "user/team";
    }
}