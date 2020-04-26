package com.company.collabcode.controller;

import com.company.collabcode.database.SessionCollaboratorRepository;
import com.company.collabcode.database.SessionRepository;
import com.company.collabcode.database.UserRepository;
import com.company.collabcode.model.*;
import com.google.firebase.database.DatabaseReference;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Controller
public class DashboardController {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final SessionCollaboratorRepository sessionCollaboratorRepository;

    public DashboardController(final UserRepository userRepository,
                             final SessionRepository sessionRepository,
                             final SessionCollaboratorRepository sessionCollaboratorRepository) {

        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.sessionCollaboratorRepository = sessionCollaboratorRepository;
    }


    @GetMapping("/dashboard")
    private String showDashboardPage(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                     Model model) {

        User currUser = customUserDetails.getUser();
        model.addAttribute("first_name", currUser.getFirstName());

        // Populate notifications
        List<Notification> notifications = new LinkedList<>();

        List<SessionCollaborator> sessionCollaborators = sessionCollaboratorRepository.findByUserId(currUser.getId());
        sessionCollaborators.forEach(sessionCollaborator -> {
            Session session = sessionRepository.findById(sessionCollaborator.getSessionId()).get();
            User creator = userRepository.findById(session.getCreatorId()).get();
            Notification notification = new Notification();
            notification.setText("<b>" + creator.getFirstName() + "</b> invited you to collaborate on <b>" + session.getName() + "</b>");
            notification.setUrl("/session/" + session.getId());
            notifications.add(notification);
        });
        model.addAttribute("notifications", notifications);

        return "dashboard";
    }
}
