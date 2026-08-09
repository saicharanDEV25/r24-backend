package com.r24.controller;

import com.r24.entity.ContactMessage;
import com.r24.service.ContactMessageService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
public class ContactMessageController {

    private final ContactMessageService service;

    public ContactMessageController(ContactMessageService service) {
        this.service = service;
    }

    // Public: the site's Contact Us form submits here.
    @PostMapping
    public ContactMessage saveMessage(@Valid @RequestBody ContactMessage message) {
        return service.saveMessage(message);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<ContactMessage> getAllMessages() {
        return service.getAllMessages();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ContactMessage getMessageById(@PathVariable Long id) {
        return service.getMessageById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ContactMessage updateMessage(@PathVariable Long id,
                                        @RequestBody ContactMessage message) {
        return service.updateMessage(id, message);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public String deleteMessage(@PathVariable Long id) {
        service.deleteMessage(id);
        return "Message Deleted Successfully";
    }
}