package com.r24.service;

import com.r24.entity.ContactMessage;

import java.util.List;

public interface ContactMessageService {

    ContactMessage saveMessage(ContactMessage message);

    List<ContactMessage> getAllMessages();

    ContactMessage getMessageById(Long id);

    ContactMessage updateMessage(Long id, ContactMessage message);

    void deleteMessage(Long id);
}