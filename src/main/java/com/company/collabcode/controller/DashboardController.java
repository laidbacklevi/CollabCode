package com.company.collabcode.controller;

import com.company.collabcode.model.CustomUserDetails;
import com.company.collabcode.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @GetMapping
    private String showDashboardPage(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        User currUser = customUserDetails.getUser();
        model.addAttribute("first_name", currUser.getFirstName());
        return "dashboard";
    }

}
