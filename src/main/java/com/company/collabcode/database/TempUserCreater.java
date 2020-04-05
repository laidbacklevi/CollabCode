package com.company.collabcode.database;

import com.company.collabcode.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TempUserCreater implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Delete all
        this.userRepository.deleteAll();

        // Crete users
        User abc = new User("abc@gmail.com", "abc", "abc", "abc");
        User xyz = new User("xyz@gmail.com", "xyz", "xyz", "xyz");

        System.out.println("Hello");

        List<User> users = Arrays.asList(abc, xyz);

        // Save to db
        this.userRepository.saveAll(users);
    }
}
