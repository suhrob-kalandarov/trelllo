package org.exp.trello.controllers.auth;

import lombok.RequiredArgsConstructor;
import org.exp.trello.services.EmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VerificationController {
    private final EmailService emailService;

    @GetMapping("/send-code")
    public String sendCode(@RequestParam String email) {
        String code = String.valueOf((int)(Math.random() * 9000) + 1000);
        emailService.sendVerificationCode(email, code);
        return "Verification code sent to " + email;
    }
}