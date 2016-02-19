package com.sixlabs.atsys.web.jsf.listener;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 * Default implementation for the PhaseListener interface.
 *
 * @author Arjan Tijms
 */
public abstract class DefaultPhaseListener implements PhaseListener {

    private static final long serialVersionUID = -7252366571645029385L;

    private PhaseId phaseId;

    public DefaultPhaseListener(PhaseId phaseId) {
        this.phaseId = phaseId;
    }

    @Override
    public PhaseId getPhaseId() {
        return phaseId;
    }

    @Override
    public void afterPhase(PhaseEvent event) {
        // NOOP.
    }

    @Override
    public void beforePhase(PhaseEvent event) {
        // NOOP.
    }

}
