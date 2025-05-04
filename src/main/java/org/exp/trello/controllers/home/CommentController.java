package org.exp.trello.controllers.home;

import org.exp.trello.models.entities.Comment;
import org.exp.trello.models.entities.Task;
import org.exp.trello.models.entities.User;
import org.exp.trello.repositories.CommentRepository;
import org.exp.trello.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/show/{taskId}")
    public String showComments(@PathVariable Integer taskId, Model model, @AuthenticationPrincipal User currentUser) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);

        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            model.addAttribute("task", task);
            model.addAttribute("comments", task.getComments());
            model.addAttribute("user", currentUser);
            return "task/comments";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/add/{taskId}")
    public String addComment(@PathVariable Integer taskId,
                             @RequestParam String text,
                             @AuthenticationPrincipal User currentUser,
                             RedirectAttributes redirectAttributes) {

        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if (optionalTask.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Task not found");
            return "redirect:/";
        }

        Comment comment = new Comment();
        comment.setText(text);
        comment.setUser(currentUser);

        commentRepository.save(comment);

        Task task = optionalTask.get();
        task.getComments().add(comment);
        taskRepository.save(task);

        return "redirect:/comments/show/" + taskId;
    }
}