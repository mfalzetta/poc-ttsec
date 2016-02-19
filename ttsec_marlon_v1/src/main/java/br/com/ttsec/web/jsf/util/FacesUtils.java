package br.com.ttsec.web.jsf.util;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author averri
 */
public class FacesUtils {

    public static final SelectItem EMPTY_SELECTION =
            new SelectItem(null, "Não informado");

    /**
     * @param severity
     * @param summary
     * @param details
     */
    public static void addMessage(Severity severity, String summary, String details) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(severity, summary, details));
    }// end method

    /**
     * @param summary
     * @param details
     */
    public static void addErrorMessage(String summary, String details) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, details));
    }// end method

    /**
     * @param details
     */
    public static void addErrorMessage(String details) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro: ", details));
    }// end method

    /**
     * @param summary
     * @param details
     */
    public static void addWarnMessage(String summary, String details) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, summary, details));
    }// end method

    /**
     * @param details
     */
    public static void addWarnMessage(String details) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção: ", details));
    }// end method

    /**
     * @param summary
     * @param details
     */
    public static void addInfoMessage(String summary, String details) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, summary, details));
    }// end method

    /**
     * @param details
     */
    public static void addInfoMessage(String details) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação: ", details));
    }// end method

    /**
     * @param summary
     * @param details
     */
    public static void addFatalMessage(String summary, String details) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_FATAL, summary, details));
    }// end method

    public static String getRealPath(String resource) {
        return ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(
                resource);
    }

    public static String getContextPath() {
        return ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getContextPath();
    }

    public static String getRequestURL() {
        return ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURL().
                toString();
    }

    public static String getServerName() {
        return ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getServerName();
    }

    public static int getServerPort() {
        return ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getServerPort();
    }

    public static Map<String, String> getRequestParameterMap() {
        return getExternalContext().getRequestParameterMap();
    }

    public static String getRequestParameter(String key) {
        return getRequestParameterMap().get(key);
    }

    /**
     * Retorna a URL base do servidor, ex.: 'http://nome-dns:porta'.
     *
     * @return
     */
    public static String getBaseURL() {
        StringBuilder sb = new StringBuilder();
        sb.append("http://");
        sb.append(getServerName());
        int port = getServerPort();
        if (port != 80) {
            sb.append(":").append(port);
        }
        return sb.toString();
    }

    /**
     * Retorna a URL da aplicação, ex.: 'http://nome-dns:porta/contexto'.
     *
     * @return
     */
    public static String getApplicationURL() {
        StringBuilder sb = new StringBuilder();
        sb.append("http://");
        sb.append(getServerName());
        int port = getServerPort();
        if (port != 80) {
            sb.append(":").append(port);
        }
        sb.append(getContextPath());
        return sb.toString();
    }

    public static HttpSession getHttpSession(boolean create) {
        return (HttpSession) getExternalContext().getSession(create);
    }

    public static HttpSession getHttpSession() {
        return (HttpSession) getExternalContext().getSession(false);
    }

    public static HttpServletRequest getHttpServletRequest() {
        return (HttpServletRequest) getExternalContext().getRequest();
    }

    public static HttpServletResponse getHttpServletResponse() {
        return (HttpServletResponse) getExternalContext().getResponse();
    }

    public static Object getFromRequest(String name) {
        return getHttpServletRequest().getAttribute(name);
    }

    public static ServletContext getServletContext() {
        return (ServletContext) getExternalContext();
    }

    public static ExternalContext getExternalContext() {
        return FacesContext.getCurrentInstance().getExternalContext();
    }

    public static boolean isValidationFailed() {
        return FacesContext.getCurrentInstance().isValidationFailed();
    }

    public static Cookie getCookie(String name) {
        HttpServletRequest req = getHttpServletRequest();
        return getCookie(name, req);
    }

    public static Cookie getCookie(String name, ServletRequest request) {
        HttpServletRequest req = (HttpServletRequest) request;
        return getCookie(name, req);
    }

    public static Cookie getCookie(String name, HttpServletRequest request) {
        Cookie cookie = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (name.equals(c.getName())) {
                    cookie = c;
                    break;
                }
            }
        }
        return cookie;
    }

    public static void invalidateCookie(String name) {
        Cookie c = new Cookie(name, null);
        c.setMaxAge(0);
        getHttpServletResponse().addCookie(c);
    }

    /**
     * Informa se a requisição e do tipo posback (requisições feitas que não
     * alteram a página de origem.
     *
     * @return true/false
     */
    public static boolean isPostback() {
        return FacesContext.getCurrentInstance().isPostback();
    }

    /**
     * Indica se é a primeira requisição.
     *
     * @return true/false.
     */
    public static boolean isFirstRender() {
        return !isPostback();
    }

    /**
     * Insere o objeto no escopo de flash do JSF, sob a chave informada.
     *
     * @param key
     * @param obj
     */
    public static void putToFlash(String key, Object obj) {
        getExternalContext().getFlash().put(key, obj);
    }

    /**
     * Obtém o objeto do escopo de flash do JSF, a partir da chave informada.
     *
     * @param key
     * @return
     */
    public static Object getFromFlash(String key) {
        return getExternalContext().getFlash().get(key);
    }

    /**
     * Trata o caso de navegação do JSF.
     *
     * @param outcome
     */
    public static void handleNavigation(String outcome) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ctx.getApplication().getNavigationHandler().handleNavigation(ctx, null, outcome);
    }

}
