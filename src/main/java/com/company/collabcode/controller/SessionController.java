package com.company.collabcode.controller;

import com.company.collabcode.database.SessionRepository;
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

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/session")
public class SessionController {

    private final SessionRepository sessionRepository;

    private final DatabaseReference databaseReference;

    public SessionController(final SessionRepository sessionRepository, final DatabaseReference databaseReference) {
        this.sessionRepository = sessionRepository;
        this.databaseReference = databaseReference;
    }

    @GetMapping("/new")
    @ResponseBody
    private String createNewSession(@AuthenticationPrincipal CustomUserDetails customUserDetails, HttpServletRequest httpServletRequest) {
        User currUser = customUserDetails.getUser();

        // Create a new session and redirect to that session
        String sessionName = httpServletRequest.getParameter("session_name");
        String newFirebaseDatabaseId = databaseReference.push().getKey();
        long sessionCreatorId = currUser.getId();


        return "redirect:/session/" + "SESSION_ID_HERE";
    }

}
