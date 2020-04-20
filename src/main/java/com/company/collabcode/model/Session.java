package com.company.collabcode.model;

import javax.persistence.*;

@Entity(name = "session")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "creator_id", nullable = false)
    private long creatorId;

    public Session() {
    }

    public Session(String name, long creatorId) {
        this.name = name;
        this.creatorId = creatorId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }
}
