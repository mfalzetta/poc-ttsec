package com.sixlabs.atsys.web.jsf.model;

import com.sixlabs.atsys.domain.ApplicationException;
import com.sixlabs.atsys.domain.Config;
import com.sixlabs.atsys.service.SecurityService;
import com.sixlabs.atsys.web.filter.SecurityFilter;
import com.sixlabs.atsys.web.jsf.util.FacesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;

/**
 * @author averri
 */
@ViewScoped
@ManagedBean
public class LogonMB implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(LogonMB.class);

    @Inject
    private Config config;

    @Inject
    private SecurityService securityService;

    // Login informado na UI.
    private String username;

    // Senha informada na UI.
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Realiza o login no sistema.
     *
     * @return O nome da action.
     */
    public String logon() {
        try {
            // Realiza o login.
            securityService.login(username, password);
            // Obtém o usuário logado.
            final String user = securityService.getLoggedUser();
            // Mensagem de boas vindas.
            FacesUtils.infoMsg("Bem vindo(a) " + user + "!");

            // Obtém a URL soliciada antes do logon, se houver.
            final String requestedPath = (String) FacesUtils.getHttpSession(true)
                    .getAttribute(SecurityFilter.REQUESTED_PATH_KEY);

            // Redireciona para a URL requisitada se houver.
            if (requestedPath != null) return  requestedPath + "?faces-redirect=true";
            // Redireciona para a página de pós-login caso contrário.
            else return  config.getAfterLogonURL() + "?faces-redirect=true";

        } catch (ApplicationException e) {
            FacesUtils.warnMsg(e.getMessage());

        } catch (Exception e) {
            LOG.error("Erro inesperado.", e);
            FacesUtils.error("unexpected_error");
        }

        setPassword(null);

        return null;
    }

}
