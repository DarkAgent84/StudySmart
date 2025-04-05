package com.studysmart.service;

import com.studysmart.dto.CollaborationMemberResponse;
import com.studysmart.dto.CollaborationRequest;
import com.studysmart.dto.CollaborationResponse;
import com.studysmart.dto.User;
import com.studysmart.exception.CustomException;
import com.studysmart.feign.UserServiceClient;
import com.studysmart.model.Collaboration;
import com.studysmart.model.CollaborationMember;
import com.studysmart.repository.CollaborationMemberRepository;
import com.studysmart.repository.CollaborationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CollaborationServiceImpl implements CollaborationService {

    private static final Logger logger = LoggerFactory.getLogger(CollaborationServiceImpl.class);
    private final CollaborationRepository collaborationRepository;
    private final CollaborationMemberRepository memberRepository;
    private final UserServiceClient userServiceClient;
    private final NotificationService notificationService;
    private final MemberEnrichmentService memberEnrichmentService;

    @Autowired
    public CollaborationServiceImpl(
            CollaborationRepository collaborationRepository,
            CollaborationMemberRepository memberRepository,
            UserServiceClient userServiceClient,
            NotificationService notificationService,
            MemberEnrichmentService memberEnrichmentService) {
        this.collaborationRepository = collaborationRepository;
        this.memberRepository = memberRepository;
        this.userServiceClient = userServiceClient;
        this.notificationService = notificationService;
        this.memberEnrichmentService = memberEnrichmentService;
    }

    @Override
    public CollaborationResponse createCollaboration(CollaborationRequest request) {
        if (request.getName() == null || request.getCreatedBy() == null) {
            throw new CustomException("Collaboration name and createdBy fields are required");
        }

        Collaboration collaboration = Collaboration.builder()
                .name(request.getName())
                .description(request.getDescription())
                .createdBy(request.getCreatedBy())
                .createdAt(LocalDateTime.now())
                .build();

        Collaboration saved = collaborationRepository.save(collaboration);

        // Notify about new collaboration
        notificationService.notifyCollaborationUpdated(
                saved.getId(),
                "New collaboration created: " + saved.getName()
        );

        return mapToResponse(saved);
    }

    @Override
    public List<CollaborationResponse> getCollaborationsByUser(Long userId) {
        List<Collaboration> collaborations = collaborationRepository.findByCreatedBy(userId);

        if (collaborations.isEmpty()) {
            throw new CustomException("No collaborations found for user ID: " + userId);
        }

        return collaborations.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CollaborationMember addMember(Long collaborationId, CollaborationMember member) {
        if (!collaborationRepository.existsById(collaborationId)) {
            throw new CustomException("Collaboration with ID " + collaborationId + " not found");
        }

        // Check if user already exists in this collaboration
        boolean memberExists = memberRepository.findByCollaborationId(collaborationId).stream()
                .anyMatch(m -> m.getUserId().equals(member.getUserId()));

        if (memberExists) {
            throw new CustomException("User is already a member of this collaboration");
        }

        member.setCollaborationId(collaborationId);
        member.setJoinedAt(LocalDateTime.now());

        CollaborationMember savedMember = memberRepository.save(member);

        // Fetch user details to include in notification
        try {
            User user = userServiceClient.getUserById(member.getUserId());
            // Send WebSocket notification that a member joined
            notificationService.notifyMemberJoined(
                    collaborationId,
                    member.getUserId(),
                    user.getName()
            );
        } catch (Exception e) {
            logger.warn("Could not fetch user details for notification: " + e.getMessage());
            // If user service is down, still notify but with user ID instead of name
            notificationService.notifyMemberJoined(
                    collaborationId,
                    member.getUserId(),
                    "User " + member.getUserId()
            );
        }

        return savedMember;
    }

    @Override
    public List<CollaborationMemberResponse> getMembers(Long collaborationId) {
        if (!collaborationRepository.existsById(collaborationId)) {
            throw new CustomException("Collaboration with ID " + collaborationId + " not found");
        }

        List<CollaborationMember> members = memberRepository.findByCollaborationId(collaborationId);

        if (members.isEmpty()) {
            throw new CustomException("No members found for collaboration ID: " + collaborationId);
        }

        // Use the batch enrichment service to efficiently fetch user data
        return memberEnrichmentService.enrichMembersWithUserData(members);
    }

    private CollaborationResponse mapToResponse(Collaboration collaboration) {
        return CollaborationResponse.builder()
                .id(collaboration.getId())
                .name(collaboration.getName())
                .description(collaboration.getDescription())
                .createdBy(collaboration.getCreatedBy())
                .createdAt(collaboration.getCreatedAt())
                .build();
    }
}