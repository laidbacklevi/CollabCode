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

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof SessionCollaboratorIdentity) {
            SessionCollaboratorIdentity toCompareWith = (SessionCollaboratorIdentity) obj;
            return toCompareWith.getSessionId() == this.sessionId && toCompareWith.userId == this.userId;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (int) (((this.sessionId % 1000000007) * (this.userId % 1000000007)) % 1000000007);
    }
}
