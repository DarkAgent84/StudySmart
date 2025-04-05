package com.studysmart.service;

import com.studysmart.dto.CollaborationNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyMemberJoined(Long collaborationId, Long userId, String userName) {
        CollaborationNotification notification = CollaborationNotification.builder()
                .type("MEMBER_JOINED")
                .message(userName + " has joined the collaboration")
                .userId(userId)
                .collaborationId(collaborationId)
                .timestamp(System.currentTimeMillis())
                .build();

        messagingTemplate.convertAndSend("/topic/collaboration/" + collaborationId, notification);
    }

    public void notifyCollaborationUpdated(Long collaborationId, String message) {
        CollaborationNotification notification = CollaborationNotification.builder()
                .type("COLLABORATION_UPDATED")
                .message(message)
                .collaborationId(collaborationId)
                .timestamp(System.currentTimeMillis())
                .build();

        messagingTemplate.convertAndSend("/topic/collaboration/" + collaborationId, notification);
    }
}