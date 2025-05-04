package org.exp.trello.controllers.home.a;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.exp.trello.repositories.TaskColumnRepository;
import org.exp.trello.repositories.TaskRepository;
import org.exp.trello.repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/inactive")
@RequiredArgsConstructor
public class InactiveController {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final TaskColumnRepository taskColumnRepository;

    @GetMapping("/columns")
    public String columnsPage(Model model, HttpSession httpSession) {
        model.addAttribute("currentUser", httpSession.getAttribute("user"));
        model.addAttribute("inactiveUsers", taskColumnRepository.findAllByActiveFalse());
        return "inactive/inactive-columns-page";
    }

    @GetMapping("/tasks")
    public String tasksPage(Model model, HttpSession httpSession) {
        model.addAttribute("currentUser", httpSession.getAttribute("user"));
        model.addAttribute("inactiveUsers", taskRepository.findAllByActiveFalse());
        return "inactive/inactive-tasks-page";
    }

    @GetMapping("/members")
    public String membersPage(Model model, HttpSession httpSession) {
        model.addAttribute("currentUser", httpSession.getAttribute("user"));
        model.addAttribute("inactiveUsers", userRepository.findAllByActiveFalse());
        return "inactive/inactive-members-page";
    }
}
