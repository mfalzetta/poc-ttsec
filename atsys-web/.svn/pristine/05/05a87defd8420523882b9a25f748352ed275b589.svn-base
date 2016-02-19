package com.sixlabs.atsys.service;

import com.sixlabs.atsys.domain.ApplicationException;
import com.sixlabs.atsys.domain.Logged;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.jacc.PolicyContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

@Named
@ApplicationScoped
public class SecurityService implements Serializable {

    public static final String USER_KEY = "app.username";

    private static final Logger LOG = LoggerFactory.getLogger(SecurityService.class);

    @Inject
    private EmailService emailService;

    // Usuários logados.
    private final Set<String> loggedUsers;

    public SecurityService() {
        loggedUsers = new ConcurrentSkipListSet<>();
    }

    @PostConstruct
    public void init() {
        LOG.info("Iniciando o serviço...");
    }

    /**
     * Realiza o login no sistema. Qualquer problema relacionado ao estado da autenticação será comunicado
     * através da exceção {@link ApplicationException}.
     *
     * @param username O nome do usuário.
     * @param password A senha do usuário.
     */
    public void login(final String username, final String password) {
        try {
            try {

                final HttpServletRequest request = getRequest();

                // Realiza o login programático, para integração com servidor Java EE.
                request.login(username, password);

                // Obtém o username do usuário logado, se houver.
                final String login = request.getUserPrincipal().getName();

                // Informa se o usuário logado possui o papel 'ADMIN'.
                final boolean isAdmin = request.isUserInRole("ADMIN");


            } catch (ServletException e) {
                throw new ApplicationException("Nome de usuário ou senha inválidos.");
            }
            // Cria a sessão do usuário.
            createOrUpdateSession(username);

        } catch (Exception e) {
            throw new RuntimeException("Erro inesperado no login.", e);
        }
    }

    /**
     * Cria ou atualiza a sessão WEB do usuário.
     *
     * @param username O nome do usuário.
     */
    private void createOrUpdateSession(String username) {
        final HttpServletRequest request = getRequest();
        if (request != null) {
            request.getSession(true).setAttribute(USER_KEY, username);
            loggedUsers.add(username);

        } else {
            throw new RuntimeException("Não foi encontrada uma requisição HTTP.");
        }
    }

    /**
     * Realiza o logout do usuário da aplicação.
     */
    public void logout() {
        // Obtém a requisição HTTP, se houver.
        final HttpServletRequest request = getRequest();
        if (request != null) {
            try {
                final HttpSession session = request.getSession();
                // Invalida a sessão http.
                if (session != null) session.invalidate();

            } catch (Exception e) {
                LOG.warn("Falha ao fazer o logout.", e);
            }
        }
    }

    /**
     * Obtém o usuário logado. Se não houver usuário logado, retorna null.
     *
     * @return O usuário logado, ou null.
     */
    @Produces
    @Logged
    public String getLoggedUser() {
        final HttpSession session = getSession(getRequest());
        if (session != null) {
            final String username = (String) session.getAttribute(USER_KEY);
            if (username != null && loggedUsers.contains(username)) return username;
        }
        return null;
    }

    /**
     * Observa quando uma sessão HTTP é destruída. Este método é importante para remover os usuários
     * logados do mapa interno.
     *
     * @param event O evento que denota a sessão HTTP destruída.
     */
    public void onSessionDestroyed(@Observes SessionDestroyed event) {
        LOG.info("{}", event);
        // Remove o usuário associado à sessão destruída.
        loggedUsers.remove(event.getUsername());
    }

    @Nullable
    private static HttpServletRequest getRequest() {
        try {
            return (HttpServletRequest) PolicyContext.getContext("javax.servlet.http.HttpServletRequest");

        } catch (Exception e) {
            // Ignora.
        }
        return null;
    }

    @Nullable
    private HttpSession getSession(HttpServletRequest request) {
        if (request != null) {
            return request.getSession();
        }
        return null;
    }

}
