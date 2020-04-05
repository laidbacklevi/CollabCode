package com.company.collabcode.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    private String showDashboardPage() {
        return "dashboard/dashboard";
    }
}
