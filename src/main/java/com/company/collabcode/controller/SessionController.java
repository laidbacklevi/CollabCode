package com.company.collabcode.controller;

import com.company.collabcode.database.SessionCollaboratorRepository;
import com.company.collabcode.database.SessionRepository;
import com.company.collabcode.database.UserRepository;
import com.company.collabcode.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.DatabaseReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class SessionController {

    private final String JDOODLE_CLIENT_ID = "9dcc71c931b5c9537c8d753088cfb7a1";
    private final String JDOODLE_CLIENT_SECRET = "4a13138389ef3d83f4322052174d2cdf7ca50920164e78cd98511c326a7fc213";
    private final String JDOODLE_ENDPOINT_URL = "https://api.jdoodle.com/v1/execute";

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final SessionCollaboratorRepository sessionCollaboratorRepository;

    private final SimpMessagingTemplate template;

    private final DatabaseReference databaseReference;

    public SessionController(final UserRepository userRepository,
                             final SessionRepository sessionRepository,
                             final SessionCollaboratorRepository sessionCollaboratorRepository,
                             final SimpMessagingTemplate template,
                             final DatabaseReference databaseReference) {

        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.sessionCollaboratorRepository = sessionCollaboratorRepository;
        this.template = template;
        this.databaseReference = databaseReference;
    }

    @GetMapping("/sessions")
    private String showAllSessions(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                   Model model) {

        User currUser = customUserDetails.getUser();

        // Get all created sessions
        List<Session> createdSessions = sessionRepository.findByCreatorId(currUser.getId());

        // Return empty list if no sessions found
        if(createdSessions == null)
            createdSessions = new LinkedList<>();

        model.addAttribute("created_sessions", createdSessions);

        // Get all sessions in which user is added as a collaborator
        List<Session> collabSessions = getSessionsWithUserAsCollaboratorFromUserId(currUser.getId());
        model.addAttribute("collab_sessions", collabSessions);

        return "sessions";
    }

    private List<Session> getSessionsWithUserAsCollaboratorFromUserId(long userId) {
        List<Session> result = new LinkedList<>();

        List<SessionCollaborator> sessionCollaboratorsList = sessionCollaboratorRepository.findByUserId(userId);
        if(sessionCollaboratorsList == null)
            return result;

        for(SessionCollaborator itr : sessionCollaboratorsList) {
            long sessionId = itr.getSessionId();
            Optional<Session> session = sessionRepository.findById(sessionId);
            session.ifPresent(result::add);
        }
        return result;
    }

    @PostMapping("/session/new")
    private String createNewSession(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            HttpServletRequest httpServletRequest) {

        User currUser = customUserDetails.getUser();

        // Create a new session and redirect to that session
        String name = httpServletRequest.getParameter("session_name");

        long creatorId = currUser.getId();

        Session createdSession = sessionRepository.save(new Session(name, creatorId));

        return "redirect:/session/" + createdSession.getId();
    }

    @GetMapping("/session/{session-id}")
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

        if(requestedSession.getCreatorId() == currUser.getId()) {
            // User is author
            model.addAttribute("is_creator", true);
        } else {
            // Check if user is a collaborator
            Optional<SessionCollaborator> optionalSessionCollaborator
                    = sessionCollaboratorRepository.findById(new SessionCollaboratorIdentity(requestedSession.getId(), currUser.getId()));
            if(!optionalSessionCollaborator.isPresent()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a collaborator of this session.");
            }
        }

        model.addAttribute("first_name", currUser.getFirstName());
        model.addAttribute("session_id", requestedSession.getId());
        model.addAttribute("session_name", requestedSession.getName());

        return "session";
    }

    // Handle REST API call to add collaborator to session
    // returns JSON response
    @PostMapping("/session/{session-id}/add-collaborator")
    @ResponseBody
    private String addCollaborator(@PathVariable("session-id") long sessionId,
                                   @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                   HttpServletRequest httpServletRequest) {

        User currUser = customUserDetails.getUser();

        Session currSession = sessionRepository.findById(sessionId).get();

        String emailAddressOfNewCollaborator = httpServletRequest.getParameter("email_address");
        User newCollaborator = userRepository.findByEmailAddress(emailAddressOfNewCollaborator);

        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String, String> result = new HashMap<>();

        if(currUser.getId() != currSession.getCreatorId()) {
            result.put("message", "illegal");
        } else if(newCollaborator == null) {
            // Illegal email address
            result.put("message", "failure");
        } else {
            if(currSession.getCreatorId() != newCollaborator.getId()) {
                SessionCollaborator addedCollaborator = new SessionCollaborator(currSession.getId(), newCollaborator.getId());
                addedCollaborator = sessionCollaboratorRepository.save(addedCollaborator);

                // Send notification to the added collaborator as well
                Notification notification = new Notification();
                notification.setText(currUser.getFirstName() + " invited you to collaborate on " + currSession.getName());
                notification.setUrl("/session/" + currSession.getId());
                template.convertAndSendToUser(newCollaborator.getEmailAddress(), "/queue/new-notification", notification);
            }
            result.put("message", "success");
        }

        String resultJSON = "";
        try {
            resultJSON = objectMapper.writeValueAsString(result);
        } catch (Exception e) {}

        return resultJSON;
    }

    @PostMapping("/session/{session-id}/delete")
    private String deleteSession(@PathVariable("session-id") long sessionId,
                                   @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        User currUser = customUserDetails.getUser();

        sessionRepository.findById(sessionId).ifPresent(session -> {
            if(currUser.getId() == session.getCreatorId()) {
                databaseReference.child(String.valueOf(sessionId)).removeValueAsync();
                sessionRepository.delete(session);
            }
        });

        return "redirect:/sessions";
    }


    @PostMapping("/session/{session-id}/remove")
    private String removeSelfFromCollaborator(@PathVariable("session-id") long sessionId,
                                 @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        User currUser = customUserDetails.getUser();

        sessionCollaboratorRepository.deleteById(new SessionCollaboratorIdentity(sessionId, currUser.getId()));

        return "redirect:/sessions";
    }

    @MessageMapping("/session/{id}/input")
    @SendTo("/session/{id}/output")
    public OutputContainer greeting(CodeContainer codeContainer) throws Exception {
        // Prepare to send code to JDOODLE to run
        HashMap<String, String> dataMap = new HashMap<>();
        dataMap.put("script", codeContainer.getCode());
        dataMap.put("language", codeContainer.getLanguage());
        dataMap.put("stdin", codeContainer.getStdin());
        dataMap.put("versionIndex", "0");
        dataMap.put("clientId", JDOODLE_CLIENT_ID);
        dataMap.put("clientSecret", JDOODLE_CLIENT_SECRET);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(dataMap, requestHeaders);

        OutputContainer outputContainer = restTemplate.postForObject(
                JDOODLE_ENDPOINT_URL,
                httpEntity,
                OutputContainer.class
        );
        outputContainer.setSourceUserFirstName(codeContainer.getSourceUserFirstName());

        return outputContainer;
    }

}
