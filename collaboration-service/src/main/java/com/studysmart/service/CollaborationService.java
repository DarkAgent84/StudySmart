package com.studysmart.service;

import com.studysmart.dto.CollaborationMemberResponse;
import com.studysmart.dto.CollaborationRequest;
import com.studysmart.dto.CollaborationResponse;
import com.studysmart.model.CollaborationMember;

import java.util.List;

public interface CollaborationService {
    CollaborationResponse createCollaboration(CollaborationRequest request);
    List<CollaborationResponse> getCollaborationsByUser(Long userId);
    CollaborationMember addMember(Long collaborationId, CollaborationMember member);
    List<CollaborationMemberResponse> getMembers(Long collaborationId);
}