package org.exp.trello.controllers.home;

import lombok.RequiredArgsConstructor;
import org.exp.trello.models.entities.Attachment;
import org.exp.trello.repositories.AttachmentRepository;
import org.exp.trello.services.AttachmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/attachment")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentRepository attachmentRepository;
    private final AttachmentService attachmentService;

    @GetMapping("/get/{id}")
    public ResponseEntity<byte[]> getAttachment(@PathVariable Integer id) {
        Optional<Attachment> attachmentOpt = attachmentRepository.findById(id);
        if (attachmentOpt.isPresent()) {
            return attachmentService.serveAttachment(attachmentOpt.get());
        }
        return ResponseEntity.notFound().build();
    }
}
