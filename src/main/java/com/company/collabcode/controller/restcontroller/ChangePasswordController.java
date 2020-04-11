package com.company.collabcode.controller.restcontroller;

import com.company.collabcode.database.UserRepository;
import com.company.collabcode.model.CustomUserDetails;
import com.company.collabcode.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
public class ChangePasswordController {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public ChangePasswordController(final PasswordEncoder passwordEncoder, final UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @PostMapping("/change-password")
    private String changePassword(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                  HttpServletRequest httpServletRequest) {
        User currUser = customUserDetails.getUser();

        String oldPassword = httpServletRequest.getParameter("old_password");
        String newPassword = httpServletRequest.getParameter("new_password");

        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, String> result = new HashMap<>();

        // Change password if old password matches
        if(passwordEncoder.matches(oldPassword, currUser.getPassword())) {
            currUser.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(currUser);
            result.put("message", "success");
        } else {
            result.put("message", "failure");
        }

        String resultJSON = "";
        try {
            resultJSON = objectMapper.writeValueAsString(result);
        } catch (Exception e) {}

        return resultJSON;
    }
}
