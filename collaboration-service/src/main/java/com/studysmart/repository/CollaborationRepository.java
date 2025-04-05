package com.studysmart.repository;

import com.studysmart.model.Collaboration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollaborationRepository extends JpaRepository<Collaboration, Long> {
    List<Collaboration> findByCreatedBy(Long userId);
}