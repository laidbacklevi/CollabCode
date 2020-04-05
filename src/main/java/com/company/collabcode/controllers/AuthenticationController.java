package com.company.collabcode.controllers;

import com.company.collabcode.database.UserRepository;
import com.company.collabcode.models.User;
import com.company.collabcode.utils.AuthenticationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AuthenticationController {

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    private String showLoginPage() {
        // If user is already logged in, redirect to dashboard
        if(AuthenticationHelper.isUserLoggedIn())
            return "redirect:/dashboard";
        return "login";
    }
/*
    @RequestMapping(value = "/signup", method= RequestMethod.GET)
    @ResponseBody
    private String showSignUpPage() {
        if(AuthenticationHelper.isUserLoggedIn())
            return "redirect:/dashboard";
        return "sign-up";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    @ResponseBody
    private String signUpNewUser() {
        // Sign up here...
        System.out.println("Signing up new user...");
        // Redirect to log in page if successful else show error message on this page
        return "log-in";
    }
    */
}