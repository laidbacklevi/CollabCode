package com.company.collabcode.controller;

import com.company.collabcode.utils.AuthenticationHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class DefaultController {

    @GetMapping
    private String showIndexPage() {
        // If user is already logged in, redirect to dashboard
        if(AuthenticationHelper.isUserLoggedIn())
            return "redirect:/dashboard";
        return "index";
    }

}
