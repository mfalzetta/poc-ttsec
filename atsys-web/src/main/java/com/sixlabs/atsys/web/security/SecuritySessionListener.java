package com.sixlabs.atsys.web.security;

import com.sixlabs.atsys.service.SecurityService;
import com.sixlabs.atsys.service.SessionDestroyed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class SecuritySessionListener implements HttpSessionListener {

    private static final Logger LOG = LoggerFactory.getLogger(SecuritySessionListener.class);

    @Inject
    private Event<SessionDestroyed> event;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        final HttpSession session = se.getSession();
        // Obtém o nome de usuário da sessão, se houver.
        final String username = (String) session.getAttribute(SecurityService.USER_KEY);
        if (username != null) {
            event.fire(new SessionDestroyed(username));
        }
    }
}
