package org.exp.trello.controllers.home;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.exp.trello.models.entities.Attachment;
import org.exp.trello.models.entities.Task;
import org.exp.trello.models.entities.User;
import org.exp.trello.models.enums.UserRole;
import org.exp.trello.repositories.TaskRepository;
import org.exp.trello.repositories.UserRepository;
import org.exp.trello.services.AttachmentService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamController {

    private final UserRepository userRepository;
    private final AttachmentService attachmentService;
    private final PasswordEncoder passwordEncoder;
    private final TaskRepository taskRepository;

    /**
     * Display the team management page with all users
     */
    @GetMapping
    public String teamPage(Model model, HttpSession httpSession) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        model.addAttribute("currentUser", httpSession.getAttribute("user"));
        return "user/team";
    }

    /**
     * Display the edit user page
     */
    @GetMapping("/edit/{id}")
    public String editUserPage(
            @PathVariable Integer id,
            Model model, RedirectAttributes redirectAttributes,
            HttpSession httpSession
    ) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found");
            return "redirect:/team";
        }

        User user = userOptional.get();
        model.addAttribute("user", user);
        model.addAttribute("allRoles", Arrays.asList(UserRole.values()));
        model.addAttribute("currentUser", httpSession.getAttribute("user"));

        return "user/edit-user";
    }

    /**
     * Update user information
     */
    @PostMapping("/update")
    public String updateUser(
            @RequestParam("userId") Integer userId,
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "verified", required = false) Boolean verified,
            @RequestParam(value = "active", required = false) Boolean active,
            @RequestParam(value = "avatar", required = false) MultipartFile avatarFile,
            RedirectAttributes redirectAttributes) {

        try {
            Optional<User> userOptional = userRepository.findById(userId);

            if (userOptional.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "User not found");
                return "redirect:/team";
            }

            User user = userOptional.get();

            // Update basic info
            user.setUsername(username);
            user.setEmail(email);

            // Update verification status
            user.setVerified(verified != null && verified);

            // Update user active status
            user.setActive(active != null && active);

            // Update password if provided
            if (password != null && !password.isEmpty()) {
                if (password.length() < 6) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Password must be at least 6 characters");
                    return "redirect:/team/edit/" + userId;
                }
                user.setPassword(passwordEncoder.encode(password));
            }

            // Update avatar if provided
            if (avatarFile != null && !avatarFile.isEmpty()) {
                try {
                    Attachment attachment;

                    // If user already has an attachment, update it
                    if (user.getAttachment() != null) {
                        attachment = user.getAttachment();
                        attachment = attachmentService.updateAttachment(attachment, avatarFile);
                    } else {
                        // Create new attachment
                        attachment = attachmentService.saveAttachment(avatarFile);
                        user.setAttachment(attachment);
                    }
                } catch (IOException e) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Failed to upload avatar: " + e.getMessage());
                    return "redirect:/team/edit/" + userId;
                }
            }

            // Save user
            userRepository.save(user);

            redirectAttributes.addFlashAttribute("successMessage", "User updated successfully");
            return "redirect:/team/edit/" + userId;

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating user: " + e.getMessage());
            return "redirect:/team/edit/" + userId;
        }
    }

    /**
     * Delete (deactivate) user
     */
    @PostMapping("/delete")
    public String deleteUser(
            @RequestParam("userId") Integer userId,
            RedirectAttributes redirectAttributes) {

        try {
            Optional<User> userOptional = userRepository.findById(userId);

            if (userOptional.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "User not found");
                return "redirect:/team";
            }

            User user = userOptional.get();

            // Soft delete by setting active to false
            user.setActive(false);

            List<Task> userTasks = taskRepository.findAllByUser(user);
            for (Task task : userTasks) {
                task.setActive(false);
            }

            taskRepository.saveAll(userTasks);
            userRepository.save(user);

            redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully");
            return "redirect:/team";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting user: " + e.getMessage());
            return "redirect:/team";
        }
    }
}