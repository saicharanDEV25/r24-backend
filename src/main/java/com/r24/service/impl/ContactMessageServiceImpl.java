package com.r24.service.impl;

import com.r24.entity.ContactMessage;
import com.r24.repository.ContactMessageRepository;
import com.r24.service.ContactMessageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactMessageServiceImpl implements ContactMessageService {

    private final ContactMessageRepository repository;

    public ContactMessageServiceImpl(ContactMessageRepository repository) {
        this.repository = repository;
    }

    @Override
    public ContactMessage saveMessage(ContactMessage message) {
        return repository.save(message);
    }

    @Override
    public List<ContactMessage> getAllMessages() {
        return repository.findAll();
    }

    @Override
    public ContactMessage getMessageById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public ContactMessage updateMessage(Long id, ContactMessage message) {

        ContactMessage existing = repository.findById(id).orElse(null);

        if (existing == null) {
            return null;
        }

        existing.setName(message.getName());
        existing.setEmail(message.getEmail());
        existing.setPhone(message.getPhone());
        existing.setSubject(message.getSubject());
        existing.setMessage(message.getMessage());
        existing.setReplied(message.getReplied());

        return repository.save(existing);
    }

    @Override
    public void deleteMessage(Long id) {
        repository.deleteById(id);
    }
}