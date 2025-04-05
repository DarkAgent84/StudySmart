package com.studysmart.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollaborationNotification {
    private String message;
    private String type;  // e.g., "MEMBER_JOINED", "MEMBER_LEFT", "COMMENT_ADDED"
    private Long userId;
    private Long collaborationId;
    private Long timestamp;
    private Object payload;  // Additional context data
}