package org.exp.trello.controllers.home.a;

import lombok.RequiredArgsConstructor;
import org.exp.trello.models.entities.Task;
import org.exp.trello.models.entities.TaskColumn;
import org.exp.trello.models.entities.User;
import org.exp.trello.repositories.TaskColumnRepository;
import org.exp.trello.repositories.TaskRepository;
import org.exp.trello.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/activate")
@RequiredArgsConstructor
public class ActivateController {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final TaskColumnRepository taskColumnRepository;

    @PostMapping("/member")
    @ResponseBody
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<Map<String, Object>> activateMember(@RequestParam("id") Integer id) {
        Map<String, Object> response = new HashMap<>();

        try {
            Optional<User> userOptional = userRepository.findById(id);

            if (userOptional.isEmpty()) {
                response.put("success", false);
                response.put("message", "Member not found");
                return ResponseEntity.badRequest().body(response);
            }

            User user = userOptional.get();
            user.setActive(true);
            userRepository.save(user);

            response.put("success", true);
            response.put("message", "Member activated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/task")
    @ResponseBody
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<Map<String, Object>> activateTask(@RequestParam("id") Integer id) {
        Map<String, Object> response = new HashMap<>();

        try {
            Optional<Task> taskOptional = taskRepository.findById(id);

            if (taskOptional.isEmpty()) {
                response.put("success", false);
                response.put("message", "Task not found");
                return ResponseEntity.badRequest().body(response);
            }

            Task task = taskOptional.get();
            task.setActive(true);
            taskRepository.save(task);

            response.put("success", true);
            response.put("message", "Task activated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/column")
    @ResponseBody
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ResponseEntity<Map<String, Object>> activateColumn(@RequestParam("id") Integer id) {
        Map<String, Object> response = new HashMap<>();

        try {
            Optional<TaskColumn> columnOptional = taskColumnRepository.findById(id);

            if (columnOptional.isEmpty()) {
                response.put("success", false);
                response.put("message", "Column not found");
                return ResponseEntity.badRequest().body(response);
            }

            TaskColumn column = columnOptional.get();
            column.setActive(true);
            taskColumnRepository.save(column);

            response.put("success", true);
            response.put("message", "Column activated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}