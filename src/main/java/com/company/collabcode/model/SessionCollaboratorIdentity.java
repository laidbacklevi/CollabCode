package com.company.collabcode.model;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Embeddable
public class SessionCollaboratorIdentity implements Serializable {
    private long sessionId;

    private long userId;

    public SessionCollaboratorIdentity() {
    }

    public SessionCollaboratorIdentity(long sessionId, long userId) {
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
