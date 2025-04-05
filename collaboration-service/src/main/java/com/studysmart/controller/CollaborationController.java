package com.studysmart.controller;

import com.studysmart.dto.CollaborationMemberResponse;
import com.studysmart.dto.CollaborationRequest;
import com.studysmart.dto.CollaborationResponse;
import com.studysmart.model.CollaborationMember;
import com.studysmart.service.CollaborationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collaborations")
public class CollaborationController {

    private final CollaborationService collaborationService;

    @Autowired
    public CollaborationController(CollaborationService collaborationService) {
        this.collaborationService = collaborationService;
    }

    @PostMapping
    public ResponseEntity<CollaborationResponse> createCollaboration(@Valid @RequestBody CollaborationRequest request) {
        CollaborationResponse response = collaborationService.createCollaboration(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CollaborationResponse>> getCollaborationsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(collaborationService.getCollaborationsByUser(userId));
    }

    @PostMapping("/{collaborationId}/members")
    public ResponseEntity<CollaborationMember> addMember(
            @PathVariable Long collaborationId,
            @RequestBody CollaborationMember member) {
        return ResponseEntity.ok(collaborationService.addMember(collaborationId, member));
    }

    @GetMapping("/{collaborationId}/members")
    public ResponseEntity<List<CollaborationMemberResponse>> getMembers(@PathVariable Long collaborationId) {
        return ResponseEntity.ok(collaborationService.getMembers(collaborationId));
    }
}