package com.company.collabcode.controllers;

import com.company.collabcode.utils.AuthenticationHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class DefaultController {

    @RequestMapping(method = RequestMethod.GET)
    private String showIndexPage() {
        // If user is already logged in, redirect to dashboard
        if(AuthenticationHelper.isUserLoggedIn())
            return "redirect:/dashboard";
        return "index";
    }

}
