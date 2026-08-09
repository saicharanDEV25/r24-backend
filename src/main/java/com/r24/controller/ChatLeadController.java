package com.r24.controller;

import com.r24.entity.ChatLead;
import com.r24.repository.ChatLeadRepository;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat-leads")
public class ChatLeadController {

    private final ChatLeadRepository repository;

    public ChatLeadController(ChatLeadRepository repository) {
        this.repository = repository;
    }

    // Public: the AI chatbot on the public site calls this to capture a lead.
    @PostMapping
    public ChatLead addLead(@Valid @RequestBody ChatLead lead) {
        lead.setCreatedAt(LocalDateTime.now());
        lead.setSeen(false);
        return repository.save(lead);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<ChatLead> getAllLeads() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/unread-count")
    public Map<String, Long> getUnreadCount() {
        return Map.of("count", repository.countBySeenFalse());
    }

    @PreAuthorize("hasRole('ADMIN')")
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

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public String deleteLead(@PathVariable Long id) {
        repository.deleteById(id);
        return "Lead Deleted Successfully";
    }
}
