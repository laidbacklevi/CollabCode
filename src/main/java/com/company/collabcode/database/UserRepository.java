package com.company.collabcode.database;

import com.company.collabcode.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findByEmailAddress(String emailAddress);
}
