package com.studysmart.service;

import com.studysmart.dto.CollaborationMemberResponse;
import com.studysmart.dto.User;
import com.studysmart.exception.UserServiceException;
import com.studysmart.feign.UserServiceClient;
import com.studysmart.model.CollaborationMember;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MemberEnrichmentService {

    private static final Logger logger = LoggerFactory.getLogger(MemberEnrichmentService.class);
    private final UserServiceClient userServiceClient;

    @Autowired
    public MemberEnrichmentService(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    /**
     * Efficiently enriches a list of collaboration members with user data by making
     * a single batch request to the user service instead of individual requests.
     */
    public List<CollaborationMemberResponse> enrichMembersWithUserData(List<CollaborationMember> members) {
        // Extract unique user IDs
        List<Long> userIds = members.stream()
                .map(CollaborationMember::getUserId)
                .distinct()
                .collect(Collectors.toList());

        try {
            // Make a single batch request to get all user data
            List<User> users = userServiceClient.getUsersByIds(userIds);

            // Create a map for quick lookup
            Map<Long, User> userMap = new HashMap<>();
            users.forEach(user -> userMap.put(user.getId(), user));

            // Map collaboration members to responses with user data
            return members.stream()
                    .map(member -> {
                        User user = userMap.get(member.getUserId());
                        return mapToMemberResponse(member, user);
                    })
                    .collect(Collectors.toList());

        } catch (UserServiceException e) {
            logger.error("Failed to retrieve user data in batch: {}", e.getMessage());

            // Fall back to individual enrichment with placeholder data
            return members.stream()
                    .map(this::createFallbackMemberResponse)
                    .collect(Collectors.toList());
        }
    }

    private CollaborationMemberResponse mapToMemberResponse(CollaborationMember member, User user) {
        if (user == null) {
            return createFallbackMemberResponse(member);
        }

        return CollaborationMemberResponse.builder()
                .id(member.getId())
                .userId(member.getUserId())
                .userName(user.getName())
                .userEmail(user.getEmail())
                .userRole(user.getRole())  // Added role field
                .joinedAt(member.getJoinedAt())
                .user(user)  // Store the complete user object
                .build();
    }

    private CollaborationMemberResponse createFallbackMemberResponse(CollaborationMember member) {
        return CollaborationMemberResponse.builder()
                .id(member.getId())
                .userId(member.getUserId())
                .userName("User details unavailable")
                .userEmail("N/A")
                .userRole("N/A")  // Added role field with fallback value
                .joinedAt(member.getJoinedAt())
                .build();
    }
}