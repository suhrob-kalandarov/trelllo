package org.exp.trello.controllers.home;

import jakarta.servlet.http.HttpSession;
import org.exp.trello.models.entities.*;
import org.exp.trello.repositories.*;
import org.exp.trello.services.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private CommentRepository commentRepository;

    @PostMapping
    public String createTask(@RequestParam String name,
                             @RequestParam(required = false) String description,
                             @RequestParam Integer columnId,
                             @RequestParam(required = false) Integer userId,
                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime deadline,
                             @RequestParam(required = false) MultipartFile image,
//                             @AuthenticationPrincipal User currentUser,
                             RedirectAttributes redirectAttributes
    ) throws IOException {

        Optional<TaskColumn> columnOpt = taskColumnRepository.findById(columnId);
        if (columnOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Column not found");
            return "redirect:/";
        }

        User assignee = null;
        if (userId != null && userId!=0) {
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
        task.setActive(true);

        if (deadline != null) {
            task.setDeadline(deadline);
        }

        if (image != null && !image.isEmpty()) {
            Attachment attachment = attachmentService.saveAttachment(image);
            task.setAttachment(attachment);
        }

        taskRepository.save(task);

        redirectAttributes.addFlashAttribute("successMessage", "Task created successfully");
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model, HttpSession session) {
        Optional<Task> taskOpt = taskRepository.findById(id);
        User currentUser = (User) session.getAttribute("user");
        if (taskOpt.isPresent()) {
            model.addAttribute("task", taskOpt.get());
            model.addAttribute("columns", taskColumnRepository.findAll());
            model.addAttribute("users", userRepository.findAll());
            model.addAttribute("currentUser", currentUser);
            return "task/edit-task";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/update/{id}")
    public String updateTask(@PathVariable Integer id,
                             @RequestParam String name,
                             @RequestParam(required = false) String description,
                             @RequestParam Integer columnId,
                             @RequestParam(required = false) Integer userId,
                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime deadline,
                             @RequestParam(required = false) MultipartFile image,
                             RedirectAttributes redirectAttributes) throws IOException {

        Optional<Task> taskOpt = taskRepository.findById(id);
        if (taskOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Task not found");
            return "redirect:/";
        }

        Task task = taskOpt.get();
        task.setName(name);
        task.setDescription(description);

        Optional<TaskColumn> columnOpt = taskColumnRepository.findById(columnId);
        columnOpt.ifPresent(task::setColumn);

        if (userId != null && userId!=0) {
            Optional<User> userOpt = userRepository.findById(userId);
            userOpt.ifPresent(task::setUser);
        }else{
            task.setUser(null);
        }

        if (deadline != null) {
            task.setDeadline(deadline);
        }

        if (image != null && !image.isEmpty()) {
            Attachment attachment = attachmentService.saveAttachment(image);
            task.setAttachment(attachment);
        }

        taskRepository.save(task);

        redirectAttributes.addFlashAttribute("successMessage", "Task updated successfully");
        return "redirect:/";
    }

    @PostMapping("/move")
    @ResponseBody
    public String moveTask(@RequestParam Integer taskId, @RequestParam Integer columnId) {
        Optional<Task> taskOpt = taskRepository.findById(taskId);
        Optional<TaskColumn> columnOpt = taskColumnRepository.findById(columnId);

        if (taskOpt.isPresent() && columnOpt.isPresent()) {
            Task task = taskOpt.get();
            task.setColumn(columnOpt.get());
            taskRepository.save(task);
            return "{\"success\": true}";
        }

        return "{\"success\": false}";
    }

    @PostMapping("/delete/{id}")
    public String deleteColumn(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        System.out.println("id = " + id);
        Optional<Task> optionalTask = taskRepository.findByIdWithComments(id);

        if (optionalTask.isEmpty()) {
            return "index";
        }

        Task task = optionalTask.get();

        if (!task.getComments().isEmpty()) {
            for (Comment comment : task.getComments()) {
                comment.setActive(false);
            }
            commentRepository.saveAll(task.getComments());
        }
        task.setActive(false);
        taskRepository.save(task);

        redirectAttributes.addFlashAttribute("successMessage", "Column deleted successfully");

        return "redirect:/";
    }

    @PostMapping("/extend-deadline")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> extendDeadline(
            @RequestParam("id") Integer id,
            @RequestParam("newDeadline") String newDeadlineStr,
            @RequestParam(value = "reason", required = false) String reason) {

        Map<String, Object> response = new HashMap<>();

        try {
            Optional<Task> taskOptional = taskRepository.findById(id);

            if (taskOptional.isEmpty()) {
                response.put("success", false);
                response.put("message", "Task not found");
                return ResponseEntity.badRequest().body(response);
            }

            Task task = taskOptional.get();

            // Parse the new deadline
            LocalDateTime newDeadline = LocalDateTime.parse(newDeadlineStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            // Update the task deadline
            task.setDeadline(newDeadline);
            taskRepository.save(task);

            response.put("success", true);
            response.put("message", "Deadline extended successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
