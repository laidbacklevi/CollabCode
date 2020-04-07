package com.company.collabcode.controllers;

import com.company.collabcode.database.UserRepository;
import com.company.collabcode.models.User;
import com.company.collabcode.utils.AuthenticationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AuthenticationController {
    @Autowired
    private UserRepository userRepository;

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
    private String signUpNewUser(HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes) {
        // Sign up here...
        // System.out.println("Signing up new user...");
        // Redirect to log in page if successful else show error message on this page
        String emailAddress = httpServletRequest.getParameter("email_address");
        String password = httpServletRequest.getParameter("password");
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
}