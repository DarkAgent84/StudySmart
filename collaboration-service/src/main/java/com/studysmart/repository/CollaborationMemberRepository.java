package com.studysmart.repository;

import com.studysmart.model.CollaborationMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollaborationMemberRepository extends JpaRepository<CollaborationMember, Long> {
    List<CollaborationMember> findByCollaborationId(Long collaborationId);
    List<CollaborationMember> findByUserId(Long userId);
}