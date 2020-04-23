package com.company.collabcode.database;

import com.company.collabcode.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByCreatorId(long creatorId);
}