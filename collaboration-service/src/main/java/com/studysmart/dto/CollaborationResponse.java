package com.studysmart.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollaborationResponse {
    private Long id;
    private String name;
    private String description;
    private Long createdBy;
    private LocalDateTime createdAt;
}