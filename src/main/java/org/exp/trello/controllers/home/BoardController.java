package org.exp.trello.controllers.home;

import org.exp.trello.models.entities.Task;
import org.exp.trello.models.entities.TaskColumn;
import org.exp.trello.repositories.TaskColumnRepository;
import org.exp.trello.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/")
    public String showBoard(Model model) {

        List<TaskColumn> activeColumns = taskColumnRepository.findAllByInActiveTrueOrderByPositionAsc();
        List<Task> activeTasks = taskRepository.findAllByInActiveTrue();

        model.addAttribute("activeColumns", activeColumns);
        model.addAttribute("activeTasks", activeTasks);

        return "index";
    }
}