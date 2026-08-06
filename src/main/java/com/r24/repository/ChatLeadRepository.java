package com.r24.repository;

import com.r24.entity.ChatLead;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatLeadRepository extends JpaRepository<ChatLead, Long> {

    List<ChatLead> findAllByOrderByCreatedAtDesc();

    long countBySeenFalse();
}
