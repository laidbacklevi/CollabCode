package com.company.collabcode.controller;

import com.company.collabcode.database.UserRepository;
import com.company.collabcode.model.CustomUserDetails;
import com.company.collabcode.model.User;
import com.company.collabcode.utils.AuthenticationHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Controller
public class AuthenticationController {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public AuthenticationController(final PasswordEncoder passwordEncoder, final UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    private String showLoginPage() {
        // If user is already logged in, redirect to dashboard
        if(AuthenticationHelper.isUserLoggedIn())
            return "redirect:/dashboard";
        return "login";
    }

    @GetMapping("/login-error")
    private String showLoginPageWithError(Model model) {
        // If user is already logged in, redirect to dashboard
        if(AuthenticationHelper.isUserLoggedIn())
            return "redirect:/dashboard";
        model.addAttribute("error", true);
        return "login";
    }

    @GetMapping("/signup")
    private String showSignUpPage() {
        if(AuthenticationHelper.isUserLoggedIn())
            return "redirect:/dashboard";
        return "signup";
    }

    @GetMapping("/signup-error")
    private String showSignUpPageWithError(Model model) {
        if(AuthenticationHelper.isUserLoggedIn())
            return "redirect:/dashboard";
        model.addAttribute("error", true);
        return "signup";
    }

    @PostMapping("/signup")
    private String signUpNewUser(HttpServletRequest httpServletRequest,
                                 RedirectAttributes redirectAttributes) {

        String emailAddress = httpServletRequest.getParameter("email_address");
        String password = passwordEncoder.encode(httpServletRequest.getParameter("password"));
        String firstName = httpServletRequest.getParameter("first_name");
        String lastName = httpServletRequest.getParameter("last_name");

        // Email already exists
        if(userRepository.findByEmailAddress(emailAddress) != null) {
            return "redirect:/signup-error";
        }

        User user = new User(emailAddress, password, firstName, lastName);
        userRepository.save(user);

        redirectAttributes.addFlashAttribute("registration_success", true);
        return "redirect:/login";
    }

    // Handle REST API call to change password
    // returns JSON response
    @PostMapping("/change-password")
    @ResponseBody
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
            currUser = userRepository.save(currUser);
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