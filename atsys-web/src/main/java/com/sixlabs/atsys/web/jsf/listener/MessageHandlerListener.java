package com.sixlabs.atsys.web.jsf.listener;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Permite que mensagens do JSF sobrevivam a redirecionamentos.
 *
 * @author averri
 */
public class MessageHandlerListener implements PhaseListener {

    //  a name to save messages in the session under.
    private static final String sessionToken = "MULTI_PAGE_MESSAGES_SUPPORT";

    // Return the identifier of the request processing phase during which this
    // listener is interested in processing PhaseEvent events.
    @Override
    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }

    // Handle a notification that the processing for a particular phase of the
    // request processing lifecycle is about to begin.
    @Override
    public void beforePhase(PhaseEvent event) {
        if (event.getPhaseId() == PhaseId.RENDER_RESPONSE) {
            FacesContext facesContext = event.getFacesContext();
            restoreMessages(facesContext);
        }
    }

    // Handle a notification that the processing for a particular phase has just been completed.
    @Override
    public void afterPhase(PhaseEvent event) {

        if (event.getPhaseId() == PhaseId.APPLY_REQUEST_VALUES ||
                event.getPhaseId() == PhaseId.PROCESS_VALIDATIONS || event.
                getPhaseId() == PhaseId.INVOKE_APPLICATION) {

            FacesContext facesContext = event.getFacesContext();
            saveMessages(facesContext);
        }

    }

    /**
     * Remove the messages that are not associated with any particular component
     * from the faces context and store them to the user's session.
     */
    // return the number of removed messages.
    private int saveMessages(FacesContext facesContext) {
        // remove messages from the context
        List messages = new ArrayList();
        for (Iterator i = facesContext.getMessages(null); i.hasNext(); ) {
            messages.add(i.next());
            i.remove();
        }
        // store them in the session
        if (messages.isEmpty()) {
            return 0;
        }
        Map sessionMap = facesContext.getExternalContext().getSessionMap();
        // if there already are messages
        List existingMessages = (List) sessionMap.get(sessionToken);
        if (existingMessages != null) {
            existingMessages.addAll(messages);
        } else {
            sessionMap.put(sessionToken, messages); // if these are the first messages
        }
        return messages.size();
    }

    /**
     * Remove the messages that are not associated with any particular component
     * from the user's session and add them to the faces context.
     */
    //return the number of removed messages.
    private int restoreMessages(FacesContext facesContext) {
        // remove messages from the session
        Map sessionMap = facesContext.getExternalContext().getSessionMap();
        List messages = (List) sessionMap.remove(sessionToken);
        // store them in the context
        if (messages == null) {
            return 0;
        }
        int restoredCount = messages.size();
        for (Object message : messages) {
            facesContext.addMessage(null, (FacesMessage) message);
        }
        return restoredCount;
    }

}
