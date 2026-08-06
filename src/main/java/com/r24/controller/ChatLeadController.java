package com.r24.controller;

import com.r24.entity.ChatLead;
import com.r24.repository.ChatLeadRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat-leads")
@CrossOrigin(origins = "*")
public class ChatLeadController {

    private final ChatLeadRepository repository;

    public ChatLeadController(ChatLeadRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ChatLead addLead(@RequestBody ChatLead lead) {
        lead.setCreatedAt(LocalDateTime.now());
        lead.setSeen(false);
        return repository.save(lead);
    }

    @GetMapping
    public List<ChatLead> getAllLeads() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    @GetMapping("/unread-count")
    public Map<String, Long> getUnreadCount() {
        return Map.of("count", repository.countBySeenFalse());
    }

    @PutMapping("/mark-all-seen")
    public String markAllSeen() {
        List<ChatLead> unseen = repository.findAllByOrderByCreatedAtDesc()
                .stream()
                .filter(lead -> !Boolean.TRUE.equals(lead.getSeen()))
                .toList();

        unseen.forEach(lead -> lead.setSeen(true));
        repository.saveAll(unseen);

        return "Marked " + unseen.size() + " leads as seen";
    }

    @DeleteMapping("/{id}")
    public String deleteLead(@PathVariable Long id) {
        repository.deleteById(id);
        return "Lead Deleted Successfully";
    }
}
