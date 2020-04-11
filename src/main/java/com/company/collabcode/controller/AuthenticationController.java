package com.company.collabcode.controller;

import com.company.collabcode.database.UserRepository;
import com.company.collabcode.model.User;
import com.company.collabcode.utils.AuthenticationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

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
}