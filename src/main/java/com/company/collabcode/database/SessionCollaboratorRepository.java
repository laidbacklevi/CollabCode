package com.company.collabcode.database;

import com.company.collabcode.model.SessionCollaborator;
import com.company.collabcode.model.SessionCollaboratorIdentity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionCollaboratorRepository extends JpaRepository<SessionCollaborator, SessionCollaboratorIdentity> {
}
