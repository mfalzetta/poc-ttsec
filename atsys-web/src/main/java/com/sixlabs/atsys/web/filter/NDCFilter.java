package com.sixlabs.atsys.web.filter;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Filtro responsável por registrar informações importantes no sistema de logging. As informações inseridas são:
 * <ul>
 * <li>ID único da requisição (rid);</li>
 * <li>Nome do usuário, se estiver logado (username).</li>
 * <li>IP de acesso do usuário (ip).</li>
 * </ul>
 *
 * @author Alexandre Verri
 */
@WebFilter(filterName = "NDCFilter",
        urlPatterns = {"/*"}, asyncSupported = true)
public class NDCFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(NDCFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Insere o identificador da requisição no MDC.
        MDC.put("rid", RandomStringUtils.randomAlphanumeric(4));
        // Insere o IP do cliente no MDC.
        MDC.put("ip", request.getRemoteAddr());
        // Obtém o usuário logado, se houver.
        final String username = ((HttpServletRequest) request).getRemoteUser();
        // Adiciona o nome do usuário no contexto MDC.
        MDC.put("username", username != null ? username : "<guest>");

        try {
            chain.doFilter(request, response);

        } catch (Throwable t) {
            LOG.error("Erro no processamento da requisição.", t);
        }

        // Limpa o contexto de diagnóstico.
        MDC.clear();
    }

    @Override
    public void destroy() {
    }
}
