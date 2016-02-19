package com.sixlabs.atsys.web.filter;

import com.sixlabs.atsys.service.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "SecurityFilter", urlPatterns = {"/*"}, asyncSupported = true)
public class SecurityFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityFilter.class);

    public static final String REQUESTED_PATH_KEY = "app.requestedURL";

    private static final String LOGIN_PATH = "/login.jsf";

    private static final String PASSWORD_CHANGE_PATH = "/secured/change-password.jsf";

    private FilterConfig filterConfig = null;

    @Inject
    private SecurityService securityService;

    public SecurityFilter() {
    }

    private void doBeforeProcessing(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        // Obtém o usuário logado.
        final String username = securityService.getLoggedUser();
        // Obtém o path solicitado.
        final String path = request.getServletPath();

        //===========================
        // Se o usuário não estiver logado,
        //===========================

        if (username == null) {

            if (!path.startsWith(LOGIN_PATH)) {
                request.getSession(true).setAttribute(REQUESTED_PATH_KEY, path);
            }

            if (path.startsWith("/secured")) {
                request.getRequestDispatcher(LOGIN_PATH).forward(request, response);
            }
        }

        //===========================
        // Se o usuário estiver logado,
        //===========================

        else {

            if(path.endsWith("/logout")) {
                return;
            }

//            if (path.startsWith("/secured/admin") && !username.hasRole(Role.ADMIN)) {
//                request.getRequestDispatcher("/denied.jsf").forward(request, response);
//            }

            // Se o usuário precisa trocar a senha,
//            if (username.isMustChangePassword()) {
//                // Verifica se a URL atual não é a URL de troca de senha.
//                if (!path.endsWith(PASSWORD_CHANGE_PATH)) {
//                    // Envia  a página de troca de senha.
//                    request.getRequestDispatcher(PASSWORD_CHANGE_PATH).forward(request, response);
//                }
//            }
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        final HttpServletRequest req = (HttpServletRequest) request;
        final HttpServletResponse res = (HttpServletResponse) response;

        doBeforeProcessing(req, res);

        try {
            chain.doFilter(req, res);

        } catch (Throwable t) {
            LOG.error("Erro no processamento da requisição.", t);
        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

}
