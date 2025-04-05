package com.studysmart.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollaborationRequest {
    @NotBlank(message = "Collaboration name must not be blank")
    private String name;
    private String description;
    @NotNull(message = "CreatedBy (user ID) must not be null")
    private Long createdBy;
}