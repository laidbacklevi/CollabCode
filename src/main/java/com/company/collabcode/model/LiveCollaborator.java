package com.company.collabcode.model;

// DTO
public class LiveCollaborator {
    // Only ID and first name of user needed
    private long id;
    private String firstName;

    public LiveCollaborator() {}

    public LiveCollaborator(final User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public int hashCode() {
        return (int) (((id % 1000000007) * (firstName.hashCode() % 1000000007)) % 1000000007);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof LiveCollaborator) {
            LiveCollaborator toComapreWith = (LiveCollaborator) obj;
            return id == toComapreWith.id && firstName.equals(toComapreWith.firstName);
        }
        return false;
    }
}
