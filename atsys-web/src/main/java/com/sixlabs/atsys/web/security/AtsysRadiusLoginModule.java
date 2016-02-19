package com.sixlabs.atsys.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.security.acl.Group;
import java.util.Map;

public class AtsysRadiusLoginModule implements LoginModule {

    private static final Logger LOG = LoggerFactory.getLogger(AtsysRadiusLoginModule.class);

    // O usuário sendo autenticado.
    private Subject subject;

    // O callback para obtenção de credenciais de login.
    private CallbackHandler callbackHandler;

    // Usado para receber parâmetros de configuração.
    private Map<String, ?> options;

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        this.options = options;
    }

    @Override
    public boolean login() throws LoginException {

        final NameCallback nameCallback = new NameCallback("Nome de usuário");
        final PasswordCallback passwordCallback = new PasswordCallback("Senha do usuário", false);
        final Callback callbacks[] = new Callback[]{nameCallback, passwordCallback};

        try {
            // Invoca o callbackHandler para obter o nome de usuário e senha.
            this.callbackHandler.handle(callbacks);
            // Obtém o nome de usuário.
            final String username = nameCallback.getName();
            // Obtém o password do usuário.
            final String password = new String(passwordCallback.getPassword());

            LOG.debug("Realizando login - Username: {}", username);

            // TODO: Chamar o servidor Radius neste ponto, para autenticar o usuário.

            // ==================================================================
            // Se houver sucesso na autenticação do username/password, configura o usuário e papéis no subject.
            // ==================================================================

            final Group group = new SimpleGroup("CallerPrincipal");
            group.addMember(new SimplePrincipal(username));
            this.subject.getPrincipals().add(group);

//            final SimpleGroup rGroup = new SimpleGroup("Roles");
//            roles.forEach(rGroup::addMember);
//            this.subject.getPrincipals().add(rGroup);

            return true;

        } catch (Exception e) {
            LOG.info("Erro inesperado no login.", e);
        }

        return false;
    }

    @Override
    public boolean commit() throws LoginException {
        return true;
    }

    @Override
    public boolean abort() throws LoginException {
        return true;
    }

    @Override
    public boolean logout() throws LoginException {
        return true;
    }
}
