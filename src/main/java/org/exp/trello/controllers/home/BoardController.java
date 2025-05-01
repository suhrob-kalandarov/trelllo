package org.exp.trello.controllers.home;

import jakarta.servlet.http.HttpSession;
import org.exp.trello.models.entities.Task;
import org.exp.trello.models.entities.TaskColumn;
import org.exp.trello.models.entities.User;
import org.exp.trello.repositories.TaskColumnRepository;
import org.exp.trello.repositories.TaskRepository;
import org.exp.trello.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class BoardController {

    @Autowired
    private TaskColumnRepository taskColumnRepository;

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String showBoard(Model model, @AuthenticationPrincipal User user, HttpSession session) {

        List<TaskColumn> activeColumns = taskColumnRepository.findAllByActiveTrueOrderByPositionAsc();
        List<Task> activeTasks = taskRepository.findAllByActiveTrue();
        List<User> users = userRepository.findAll();

        model.addAttribute("activeColumns", activeColumns);
        model.addAttribute("activeTasks", activeTasks);
        model.addAttribute("users", users);
        model.addAttribute("currentUser", user);
        session.setAttribute("user",user);

        return "index";
    }
}