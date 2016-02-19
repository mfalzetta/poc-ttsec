package com.sixlabs.atsys.web.jsf.model;

import com.sixlabs.atsys.domain.Email;
import com.sixlabs.atsys.repository.EmailRep;
import com.sixlabs.atsys.web.jsf.util.FacesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

@ManagedBean
@ViewScoped
public class EmailMB implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(EmailMB.class);

    @Inject
    private EmailRep emailRep;

    // Lista de emails disponíveis.
    private List<Email> emails;

    // O email selecionado para visualização.
    private Email email;

    @PostConstruct
    public void init() {
        FacesUtils.run(LOG, () -> {
            // TODO: Otimizar, e listar os últimos emails recebidos inicialmente.
            emails = emailRep.listAll();
        });
    }

    public List<Email> getEmails() {
        return emails;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }
}
