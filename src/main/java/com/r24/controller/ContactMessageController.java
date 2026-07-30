package com.r24.controller;

import com.r24.entity.ContactMessage;
import com.r24.service.ContactMessageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = "*")
public class ContactMessageController {

    private final ContactMessageService service;

    public ContactMessageController(ContactMessageService service) {
        this.service = service;
    }

    @PostMapping
    public ContactMessage saveMessage(@RequestBody ContactMessage message) {
        return service.saveMessage(message);
    }

    @GetMapping
    public List<ContactMessage> getAllMessages() {
        return service.getAllMessages();
    }

    @GetMapping("/{id}")
    public ContactMessage getMessageById(@PathVariable Long id) {
        return service.getMessageById(id);
    }

    @PutMapping("/{id}")
    public ContactMessage updateMessage(@PathVariable Long id,
                                        @RequestBody ContactMessage message) {
        return service.updateMessage(id, message);
    }

    @DeleteMapping("/{id}")
    public String deleteMessage(@PathVariable Long id) {
        service.deleteMessage(id);
        return "Message Deleted Successfully";
    }
}