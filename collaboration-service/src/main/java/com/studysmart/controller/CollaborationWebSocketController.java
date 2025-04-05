package com.studysmart.controller;

import com.studysmart.dto.CollaborationNotification;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class CollaborationWebSocketController {

    @MessageMapping("/collaboration/{collaborationId}")
    @SendTo("/topic/collaboration/{collaborationId}")
    public CollaborationNotification sendCollaborationUpdate(
            @DestinationVariable Long collaborationId,
            CollaborationNotification notification) {

        // You can add logic here to process the notification before broadcasting
        // For example, add timestamps, validate content, etc.
        notification.setTimestamp(System.currentTimeMillis());

        return notification;
    }
}