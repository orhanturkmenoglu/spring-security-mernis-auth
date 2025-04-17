package com.example.spring_security_mernis_auth.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEvents {

    Logger log = LoggerFactory.getLogger(AuthenticationEvents.class);

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent successEvent) {
        log.info("Authentication successful for user: {}", successEvent.getAuthentication().getName());
    }

    @EventListener
    public void onAuthenticationFailure(AbstractAuthenticationFailureEvent failureEvent) {
        log.error("Login failed for user: {} due to: {}", failureEvent.getAuthentication().getName(),
                failureEvent.getException().getMessage());
    }
}
