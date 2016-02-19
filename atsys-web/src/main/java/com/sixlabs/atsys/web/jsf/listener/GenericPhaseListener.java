package com.sixlabs.atsys.web.jsf.listener;

import com.sixlabs.atsys.web.jsf.util.FacesUtils;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import static com.sixlabs.atsys.web.jsf.util.FacesUtils.isValidationsFailed;

/**
 * @author averri
 */
public class GenericPhaseListener implements PhaseListener {

    @Override
    public void afterPhase(PhaseEvent event) {
        // Se houve falha de validação,
        if (event.getPhaseId() == PhaseId.PROCESS_VALIDATIONS && isValidationsFailed()) {
            FacesUtils.warnMsg("Existem dados incorretos ou faltando. "
                    + "Por favor verifique os campos em vermelho.");
        }
    }

    @Override
    public void beforePhase(PhaseEvent event) {
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }
}
