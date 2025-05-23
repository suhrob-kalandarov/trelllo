package org.exp.trello.controllers.authcontrollers;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.exp.trello.models.entities.User;
import org.exp.trello.repositories.UserRepository;
import org.exp.trello.services.EmailService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
public class VerificationController {
    private final EmailService emailService;
    private final UserRepository userRepository;

    @Transactional
    @GetMapping("/send-code")
    public String sendCode(HttpSession session) {
        Object user = session.getAttribute("user");
        if (user == null) {
            return "redirect:/auth/login";
        }
        User sessionUser = (User) user;
        String code = String.valueOf((int)(Math.random() * 9000) + 1000);
        Thread thread = new Thread(() -> {
            emailService.sendVerificationCode(sessionUser.getEmail(), code);
        });
        thread.start();
        session.setAttribute("verificationCode", code);
        return "auth/verification";
    }

    @Transactional
    @PostMapping("/verify")
    public String verify(HttpSession session, @RequestParam String verificationCode, Model model) {
        Object user = session.getAttribute("user");

        if (user == null) {
            return "redirect:/auth/login";
        }

        User registeredUser = (User) user;

        String code = (String) session.getAttribute("verificationCode");
        if (!code.equals(verificationCode)) {
            model.addAttribute("error", "Verification code is incorrect. Please try again");
            return "auth/verification";
        }

        registeredUser.setActive(true);
        registeredUser.setVerified(true);
        User save = userRepository.save(registeredUser);

        System.out.println("registeredUser = " + registeredUser);
        System.out.println("save = " + save);

        return "auth/login";
    }

}
