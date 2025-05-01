package org.exp.trello.controllers.home;

import jakarta.servlet.http.HttpSession;
import org.exp.trello.models.entities.Attachment;
import org.exp.trello.models.entities.User;
import org.exp.trello.repositories.AttachmentRepository;
import org.exp.trello.repositories.CommentRepository;
import org.exp.trello.repositories.TaskRepository;
import org.exp.trello.repositories.UserRepository;
import org.exp.trello.services.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private AttachmentService attachmentService;

    @GetMapping("/{id}")
    public String showProfile(Model model, @PathVariable Integer id, HttpSession session) {
        User user = (User) session.getAttribute("user");

        model.addAttribute("user", user);

        Integer countTask = taskRepository.findCountByUserId(id);
        Integer countComment = commentRepository.findCountByUserId(id);
        Integer countByTaskAndActiveTrue = taskRepository.findCountByUserIdAndActiveTrue(id);


        System.out.println(user);

        model.addAttribute("commentCount", countComment);
        model.addAttribute("taskCount", countTask);
        model.addAttribute("activeTaskCount", countByTaskAndActiveTrue);
        return "profile";
    }

    @PostMapping("/update")
    public String updateAvatar(@RequestParam("avatar") MultipartFile avatarFile,
                               HttpSession session,
                               @RequestParam String username,
                               RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        try {
            if (!avatarFile.isEmpty()) {
                Attachment saveAttachment = attachmentService.saveAttachment(avatarFile);
                assert user != null;
                user.setAttachment(saveAttachment);

                redirectAttributes.addFlashAttribute("successMessage", "Profile picture updated successfully");
            }
            assert user != null;
            user.setName(username);
            userRepository.save(user);
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to upload profile picture: " + e.getMessage());
        }

        return "redirect:/profile/"+user.getId();
    }
}