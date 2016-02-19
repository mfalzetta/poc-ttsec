package com.sixlabs.atsys.web.jsf.model;


import com.sixlabs.atsys.domain.Config;
import com.sixlabs.atsys.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;

import static com.sixlabs.atsys.web.jsf.util.FacesUtils.run;

/**
 * @author averri
 */
@ViewScoped
@ManagedBean
public class ConfigMB implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigMB.class);

    @Inject
    private Config config;

    @Inject
    private EmailService emailService;

    // Cópia das configurações.
    private Config copy;

    @PostConstruct
    public void init() {
        copy = config.copy();
    }

    /**
     * Salva as propriedades de configuração.
     */
    public void save() {
        run("Atualizado com sucesso. ", LOG, () -> config.update(copy));
    }

    /**
     * Envia um email de teste para o usuário logado.
     */
    public void sendEmailTest() {
//        run("Email enviado com sucesso!", LOG, () -> emailService.sendTestEmailNow(user));
    }

    /**
     * Obtém a cópia das propriedades de configuração para edição.
     *
     * @return A cópia das propriedades de configuração.
     */
    public Config getCopy() {
        return copy;
    }
}
