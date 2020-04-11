package com.company.collabcode.controller;

import com.company.collabcode.model.CustomUserDetails;
import com.company.collabcode.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/session")
public class SessionController {

    private final DatabaseReference databaseReference;

    public SessionController(final DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    @GetMapping("/new")
    @ResponseBody
    private String createNewSession(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        User currUser = customUserDetails.getUser();

        // create a new session and redirect to that session


        return "new" + currUser.getEmailAddress();
        //return "redirect:/session/" + "SESSION_ID_HERE";
    }

}
