package com.example.spring_security_mernis_auth.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationEvents {

    private final static Logger log = LoggerFactory.getLogger(AuthorizationEvents.class);

    @EventListener
    public void onAuthorizationFailure(AuthorizationDeniedEvent event) {
        log.error("Authorization failed for user: {} due to: {}", event.getAuthentication()
                .get().getName(),
                event.getAuthorizationResult().toString());
    }
}
