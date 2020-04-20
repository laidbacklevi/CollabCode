package com.company.collabcode.model;

import javax.persistence.*;

@Entity(name = "session_collaborator")
@IdClass(SessionCollaboratorIdentity.class)
public class SessionCollaborator {
    @Id
    @Column(name = "session_id", nullable = false, updatable = false)
    private long sessionId;

    @Id
    @Column(name = "user_id", nullable = false, updatable = false)
    private long userId;

    public SessionCollaborator() {
    }

    public SessionCollaborator(long sessionId, long userId) {
        this.sessionId = sessionId;
        this.userId = userId;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
