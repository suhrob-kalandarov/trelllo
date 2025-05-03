package org.exp.trello.controllers.home.a;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.exp.trello.models.entities.Task;
import org.exp.trello.repositories.TaskRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/deadlines")
@RequiredArgsConstructor
public class DeadlineController {

    private final TaskRepository taskRepository;

    @GetMapping
    public String getOverdueTasks(
            Model model,
            HttpSession session,
            @RequestParam(required = false) String sort
    ) {
        // Get current user
        model.addAttribute("currentUser", session.getAttribute("user"));

        // Get all active tasks with deadlines in the past
        List<Task> overdueTasks = taskRepository.findAllByActiveTrue()
                .stream()
                .filter(task -> task.getDeadline() != null && task.getDeadline().isBefore(LocalDateTime.now()))
                .toList();

        // Sort tasks based on parameter (default: by deadline - most overdue first)
        if (sort != null) {
            switch (sort) {
                case "name":
                    overdueTasks = overdueTasks.stream()
                            .sorted(Comparator.comparing(Task::getName))
                            .toList();
                    break;
                case "column":
                    overdueTasks = overdueTasks.stream()
                            .sorted(Comparator.comparing(task -> task.getColumn().getName()))
                            .toList();
                    break;
                case "assignee":
                    overdueTasks = overdueTasks.stream()
                            .sorted(Comparator.comparing(task ->
                                    task.getUser() != null ? task.getUser().getName() : ""))
                            .toList();
                    break;
                case "recent":
                    overdueTasks = overdueTasks.stream()
                            .sorted(Comparator.comparing(Task::getDeadline).reversed())
                            .toList();
                    break;
                default:
                    // Default: most overdue first
                    overdueTasks = overdueTasks.stream()
                            .sorted(Comparator.comparing(Task::getDeadline))
                            .toList();
                    break;
            }
        } else {
            // Default: most overdue first
            overdueTasks = overdueTasks.stream()
                    .sorted(Comparator.comparing(Task::getDeadline))
                    .toList();
        }

        // Calculate counts for dashboard
        LocalDateTime now = LocalDateTime.now();
        long criticalCount = overdueTasks.stream()
                .filter(task -> task.getDeadline() != null && task.getDeadline().isBefore(now.minusDays(7)))
                .count();

        long assignedCount = overdueTasks.stream()
                .filter(task -> task.getUser() != null)
                .count();

        long unassignedCount = overdueTasks.stream()
                .filter(task -> task.getUser() == null)
                .count();

        model.addAttribute("overdueTasks", overdueTasks);
        model.addAttribute("currentSort", sort != null ? sort : "deadline");
        model.addAttribute("now", now);
        model.addAttribute("criticalCount", criticalCount);
        model.addAttribute("assignedCount", assignedCount);
        model.addAttribute("unassignedCount", unassignedCount);

        return "task/deadlines";
    }
}