package com.company.collabcode.service;

import com.company.collabcode.database.UserRepository;
import com.company.collabcode.model.LiveCollaborator;
import com.company.collabcode.model.User;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

@Service
public class SessionEventsListener {
    private final UserRepository userRepository;
    private final SimpMessagingTemplate template;

    private HashMap<String, String> sessionIdToDestinationMap;

    private HashMap<String, List<LiveCollaborator>> liveCollaborators;

    public SessionEventsListener(final UserRepository userRepository, final SimpMessagingTemplate template) {
        this.userRepository = userRepository;
        this.template = template;
        sessionIdToDestinationMap = new HashMap<>();
        liveCollaborators = new HashMap<>();
    }

    @EventListener
    public void handleSubscribeEvent(SessionSubscribeEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());

        String destination = headers.getDestination();

        // Check destination and broadcast that a new user joined
        // This could be done more thoroughly, but will work fine for the time being
        if(destination.endsWith("/live-collaborators")) {
            sessionIdToDestinationMap.put(headers.getSessionId(), destination);

            User user = userRepository.findByEmailAddress(event.getUser().getName());

            LiveCollaborator newCollaborator = new LiveCollaborator(user);

            List<LiveCollaborator> collaborators = liveCollaborators.getOrDefault(destination, new LinkedList<>());
            collaborators.add(newCollaborator);

            liveCollaborators.put(destination, collaborators);

            template.convertAndSend(destination, collaborators);
        }
    }

    @EventListener
    public void handleDisconnectEvent(SessionDisconnectEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());

        String sessionId = headers.getSessionId();

        if(sessionIdToDestinationMap.containsKey(sessionId)) {
            String destination = sessionIdToDestinationMap.get(sessionId);
            sessionIdToDestinationMap.remove(sessionId);

            User user = userRepository.findByEmailAddress(headers.getUser().getName());

            LiveCollaborator collaboratorLeaving = new LiveCollaborator(user);

            List<LiveCollaborator> collaborators = liveCollaborators.get(destination);
            collaborators.remove(collaboratorLeaving);

            liveCollaborators.put(destination, collaborators);

            template.convertAndSend(destination, collaborators);
        }
    }
}
