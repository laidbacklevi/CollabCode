package com.company.collabcode.service;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

@Service
public class SessionEventsListener {
    private final SimpMessagingTemplate template;

    public SessionEventsListener(final SimpMessagingTemplate template) {
        this.template = template;
    }

    @EventListener
    public void handleSubscribeEvent(SessionSubscribeEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        System.out.println(headers.getSessionId() + " " + headers.getDestination());
        //template.convertAndSendToUser(event.getUser().getName(), "/queue/notify", "GREETINGS");
    }

    @EventListener
    public void handleDisconnectEvent(SessionDisconnectEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        System.out.println(headers.getSessionId() + " " + headers.getDestination());
        //template.convertAndSendToUser(event.getUser().getName(), "/queue/notify", "GREETINGS");
    }
}
