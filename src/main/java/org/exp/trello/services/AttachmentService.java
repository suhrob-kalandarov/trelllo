package org.exp.trello.services;

import org.exp.trello.models.entities.Attachment;
import org.exp.trello.repositories.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class AttachmentService {

    @Autowired
    private AttachmentRepository attachmentRepository;

    public Attachment saveAttachment(MultipartFile file) throws IOException {
        Attachment attachment = new Attachment();
        attachment.setContent(file.getBytes());
        return attachmentRepository.save(attachment);
    }

    public ResponseEntity<byte[]> serveAttachment(Attachment attachment) {
        if (attachment != null && attachment.getContent() != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // Assuming images are JPEG

            return new ResponseEntity<>(attachment.getContent(), headers, HttpStatus.OK);
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * Update an existing attachment
     */
    public Attachment updateAttachment(Attachment attachment, MultipartFile file) throws IOException {
        //attachment.setFileName(file.getOriginalFilename());
        //attachment.setFileType(file.getContentType());
        attachment.setContent(file.getBytes());
        return attachmentRepository.save(attachment);
    }

    /**
     * Find attachment by ID
     */
    public Optional<Attachment> findById(Integer id) {
        return attachmentRepository.findById(id);
    }

    /**
     * Delete attachment
     */
    public void deleteAttachment(Integer id) {
        attachmentRepository.deleteById(id);
    }
}