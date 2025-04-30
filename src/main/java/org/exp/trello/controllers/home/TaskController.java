package org.exp.trello.controllers.home;

import org.exp.trello.models.entities.*;
import org.exp.trello.repositories.*;
import org.exp.trello.services.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskColumnRepository taskColumnRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public String createTask(@RequestParam String name,
                             @RequestParam(required = false) String description,
                             @RequestParam Integer columnId,
                             @RequestParam(required = false) Integer userId,
                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime deadline,
                             @AuthenticationPrincipal User currentUser,
                             RedirectAttributes redirectAttributes
    ) throws IOException {

        Optional<TaskColumn> columnOpt = taskColumnRepository.findById(columnId);
        if (columnOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Column not found");
            return "redirect:/";
        }

        User assignee = currentUser;
        if (userId != null) {
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isPresent()) {
                assignee = userOpt.get();
            }
        }

        Task task = new Task();
        task.setName(name);
        task.setDescription(description);
        task.setColumn(columnOpt.get());
        task.setUser(assignee);

        if (deadline != null) {
            task.setDeadline(deadline);
        }

        taskRepository.save(task);

        redirectAttributes.addFlashAttribute("successMessage", "Task created successfully");
        return "redirect:/";
    }


}
