package com.company.collabcode.database;

import com.company.collabcode.model.Session;
import com.company.collabcode.model.SessionCollaborator;
import com.company.collabcode.model.SessionCollaboratorIdentity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionCollaboratorRepository extends JpaRepository<SessionCollaborator, SessionCollaboratorIdentity> {
    List<SessionCollaborator> findByUserId(long userId);
}
