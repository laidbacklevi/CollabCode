package com.company.collabcode.controller;

import com.company.collabcode.database.SessionRepository;
import com.company.collabcode.model.CustomUserDetails;
import com.company.collabcode.model.Session;
import com.company.collabcode.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@RequestMapping("/session")
public class SessionController {

    private final SessionRepository sessionRepository;

    private final DatabaseReference databaseReference;

    public SessionController(final SessionRepository sessionRepository, final DatabaseReference databaseReference) {
        this.sessionRepository = sessionRepository;
        this.databaseReference = databaseReference;
    }

    @PostMapping("/new")
    private String createNewSession(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            HttpServletRequest httpServletRequest) {

        User currUser = customUserDetails.getUser();

        // Create a new session and redirect to that session
        String name = httpServletRequest.getParameter("session_name");
        String firebaseDatabaseId = databaseReference.push().getKey();
        long creatorId = currUser.getId();

        Session createdSession = sessionRepository.save(new Session(name, firebaseDatabaseId, creatorId));

        return "redirect:/session/" + createdSession.getId();
    }

    @GetMapping("/{session-id}")
    @ResponseBody
    private String showParticularSession(@PathVariable("session-id") long sessionId,
                                         @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                         HttpServletRequest httpServletRequest,
                                         Model model) {

        User currUser = customUserDetails.getUser();

        Optional<Session> optionalSession = sessionRepository.findById(sessionId);
        if(!optionalSession.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Session not found.");
        }
        Session requestedSession = optionalSession.get();

        // show session page
        // Check if actually the author and render page differently

        return "session";
    }

}
