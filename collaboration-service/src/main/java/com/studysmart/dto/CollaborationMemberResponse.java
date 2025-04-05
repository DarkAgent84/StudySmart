package com.studysmart.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollaborationMemberResponse {
    private Long id;
    private Long userId;
    private String userName;  // fetched from user-service
    private String userEmail; // fetched from user-service
    private String userRole;  // fetched from user-service
    private LocalDateTime joinedAt;
    private User user;        // Full user object
}