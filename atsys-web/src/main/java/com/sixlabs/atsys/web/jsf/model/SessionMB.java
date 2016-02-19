package com.sixlabs.atsys.web.jsf.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ComponentSystemEvent;
import java.io.Serializable;

import static com.sixlabs.atsys.web.jsf.util.FacesUtils.isFirstRender;
import static com.sixlabs.atsys.web.jsf.util.FacesUtils.run;

@SessionScoped
@ManagedBean
public class SessionMB implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(SessionMB.class);

    private String sessionId;

    private String templateName;

    @PostConstruct
    public void init() {
        templateName = "basic";
    }

    public void preRender(ComponentSystemEvent cse) {
        run(LOG, () -> {
            if (isFirstRender()) {
                LOG.debug("Renderizando a página para sessionId={}", sessionId);
            }
        });
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
}
