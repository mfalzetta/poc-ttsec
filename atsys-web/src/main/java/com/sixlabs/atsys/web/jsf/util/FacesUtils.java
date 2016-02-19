package com.sixlabs.atsys.web.jsf.util;

import com.sixlabs.atsys.domain.utils.Executable;
import com.sixlabs.atsys.domain.utils.BundleUtils;
import com.sixlabs.atsys.domain.utils.ObjectUtils;
import com.sixlabs.atsys.web.filter.SecurityFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.context.PartialViewContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class FacesUtils {

    private static final Logger LOG = LoggerFactory.getLogger(FacesUtils.class);

    /**
     * Adiciona a mensagem de erro ao JSF.
     *
     * @param details A mensagem de erro.
     */
    public static void errorMsg(String details) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, getMsg("error"), details));
    }

    /**
     * Adiciona ao JSF a mensagem de erro, internacionalizada, associada à chave.
     *
     * @param key A chave da mensagem.
     */
    public static void error(String key) {
        errorMsg(getMsg(key));
    }

    public static void error(String key, Object... params) {
        errorMsg(getMsg(key, params));
    }

    /**
     * Adiciona a mensagem de aviso ao JSF.
     *
     * @param details A mensagem de aviso.
     */
    public static void warnMsg(String details) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, getMsg("warn"), details));
    }

    /**
     * Adiciona ao JSF a mensagem de aviso, internacionalizada, associada à chave.
     *
     * @param key A chave da mensagem.
     */
    public static void warn(String key) {
        warnMsg(getMsg(key));
    }

    /**
     * Adiciona a mensagem de informação ao JSF.
     *
     * @param details A mensagem de informação.
     */
    public static void infoMsg(String details) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, getMsg("info"), details));
    }

    /**
     * Adiciona ao JSF a mensagem de informação, internacionalizada, associada à chave.
     *
     * @param key A chave da mensagem.
     */
    public static void info(String key) {
        infoMsg(getMsg(key));
    }

    /**
     * Adiciona ao JSF a mensagem de informação, internacionalizada, associada à chave.
     * A mensagem é parametrizada.
     *
     * @param key    A chave da mensagem.
     * @param params Os parâmetros da mensagem.
     */
    public static void info(String key, Object... params) {
        infoMsg(getMsg(key, params));
    }

    public static String getRealPath(String resource) {
        return getServletContext().getRealPath(resource);
    }

    public static String getContextPath() {
        return getServletContext().getContextPath();
    }

    public static String getRequestURL() {
        return getHttpServletRequest().getRequestURL().toString();
    }

    public static String getServerName() {
        return getHttpServletRequest().getServerName();
    }

    public static int getServerPort() {
        return getHttpServletRequest().getServerPort();
    }

    public static Map<String, String> getRequestParameterMap() {
        return getExternalContext().getRequestParameterMap();
    }

    public static Optional<String> getRequestParameter(String key) {
        final Map<String, String> map = getRequestParameterMap();
        return map != null ? Optional.ofNullable(map.get(key)) : Optional.empty();
    }

    /**
     * Obtém a mensagem internacionalizada associada à chave. Se este método for chamado no contexto
     * JSF será usado o {@link java.util.Locale} do usuário, caso contrário será usado o {@link java.util.Locale} padrão.
     * Este método usa mensagem parametrizada.
     *
     * @param key    A chave da mensagem parametrizada.
     * @param params Os parâmetros da mensagem.
     * @return A mensagem internacionalizada.
     */
    public static String getMsg(String key, Object... params) {
        return MessageFormat.format(getMsg(key), params);
    }

    /**
     * Obtém a mensagem internacionalizada associada à chave. Se este método for chamado no contexto
     * JSF será usado o {@link java.util.Locale} do usuário, caso contrário será usado o {@link java.util.Locale} padrão.
     *
     * @param key A chave da mensagem.
     * @return A mensagem internacionalizada.
     */
    public static String getMsg(String key) {
        return getBundle().getString(key);
    }

    /**
     * Obtém o {@link java.util.ResourceBundle} associado ao {@link java.util.Locale} do usuário se for chamado no contexto
     * JSF. Se for chamado de fora do contexto JSF irá retornar o {@link java.util.ResourceBundle} associado ao
     * {@link java.util.Locale} padrão.
     *
     * @return O {@link java.util.ResourceBundle} associado ao {@link java.util.Locale}.
     */
    public static ResourceBundle getBundle() {
        return BundleUtils.getBundle(getLocale());
    }

    /**
     * Obtém o {@link java.util.Locale} da requisição se for chamado em um contexto JSF.
     * Caso seja chamado fora do contexto JSF, vai retornar o locale padrão.
     *
     * @return O {@link java.util.Locale} do usuário, ou o locale padrão.
     */
    public static Locale getLocale() {
        Locale locale = getExternalContext().getRequestLocale();
        return locale != null ? locale : Locale.getDefault();
    }

    /**
     * Obtém a mensagem de erro do {@link java.util.ResourceBundle} e registra a mensagem no JSF e
     * também escreve no log, com nível de erro.
     *
     * @param key    A chave da mensagem no ResourceBundle.
     * @param log O log a ser escrito.
     * @param e      A instância da exceção.
     */
    public static void logError(String key, Logger log, Exception e) {
        try {
            // Obtém a mensagem internacionalizada.
            String msg = getMsg(key);
            // Adiciona a mensagem internacionalizada ao JSF.
            errorMsg(msg);
            // Registra no log.
            ObjectUtils.logError(msg, e, log);

        } catch (Exception e2) {
            LOG.error("Erro inesperado.", e2);
        }
    }

    /**
     * Retorna a URL base do servidor, ex.: 'http://nome-dns:porta'.
     *
     * @return A URL base.
     */
    public static String getBaseURL() {
        return "http://" + getServerName() +
                (getServerPort() != 80 ? ":" + getServerPort() : "");
    }

    /**
     * Retorna a URL da aplicação, ex.: 'http://nome-dns:porta/contexto'.
     *
     * @return A URL da aplicação.
     */
    public static String getApplicationURL() {
        return getBaseURL() + getContextPath();
    }

    public static HttpSession getHttpSession(boolean create) {
        return (HttpSession) getExternalContext().getSession(create);
    }

    public static Optional<HttpSession> getHttpSession() {
        return Optional.ofNullable(getHttpSession(false));
    }

    public static HttpServletRequest getHttpServletRequest() {
        return (HttpServletRequest) getExternalContext().getRequest();
    }

    public static HttpServletResponse getHttpServletResponse() {
        return (HttpServletResponse) getExternalContext().getResponse();
    }

    public static String getRemoteAddr() {
        return getHttpServletRequest().getRemoteAddr();
    }

    public static void setSessionAttribute(String name, Object value) {
        getHttpSession(true).setAttribute(name, value);
    }

    public static void redirect(final String url) {
        try {
            getExternalContext().redirect(url);

        } catch (Exception e) {
            logError("unexpected_error", LOG, e);
        }
    }

    public static Optional<Object> getFromRequest(String name) {
        return Optional.ofNullable(getHttpServletRequest().getAttribute(name));
    }

    public static <T> Optional<T> getFromSession(String name, Class<T> clazz) {
        return Optional.ofNullable((T) getHttpSession(true).getAttribute(name));
    }

    public static ServletContext getServletContext() {
        return (ServletContext) getExternalContext().getContext();
    }

    public static ExternalContext getExternalContext() {
        return getContext().getExternalContext();
    }

    /**
     * Obtém o cookie da requisição, se não houver, retorna null.
     *
     * @param name    O nome do cookie.
     * @param request O objeto de requisição HTTP.
     * @return O cookie, se houver.
     */
    public static Optional<Cookie> getCookie(String name, ServletRequest request) {
        Cookie cookie = null;
        HttpServletRequest req = (HttpServletRequest) request;
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (name.equals(c.getName())) {
                    cookie = c;
                    break;
                }
            }
        }
        return Optional.ofNullable(cookie);
    }

    /**
     * Checa se a fase de validação do JSF falhou (devido a erros de dados em formulários).
     *
     * @return true/false.
     */
    public static boolean isValidationsFailed() {
        return getContext().isValidationFailed();
    }

    public static boolean isValidationsPassed() {
        return !isValidationsFailed();
    }

    /**
     * Informa se a requisição e do tipo posback (requisições feitas que não
     * alteram a página de origem.
     *
     * @return true/false
     */
    public static boolean isPostback() {
        return getContext().isPostback();
    }

    /**
     * Indica se é a primeira requisição.
     *
     * @return true/false.
     */
    public static boolean isFirstRender() {
        return !isPostback();
    }

    public static Flash getFlash() {
        return getExternalContext().getFlash();
    }

    /**
     * Insere o objeto no escopo de flash do JSF, sob a chave informada.
     *
     * @param key A chave.
     * @param obj O valor a ser amazenado na chave.
     */
    public static void putToFlash(String key, Object obj) {
        getFlash().put(key, obj);
    }

    public static Optional<Object> getFromFlash(String key) {
        return Optional.ofNullable(getFlash().get(key));
    }

    /**
     * Trata o caso de navegação do JSF. Não vai funcionar se for chamado de fora do contexto JSF.
     *
     * @param outcome O nome do recurso JSF.
     */
    public static void handleNavigation(String outcome) {
        FacesContext faces = FacesContext.getCurrentInstance();
        // Se houver o contexto,
        if (faces != null) {
            faces.getApplication().getNavigationHandler().handleNavigation(faces, null, outcome);
        }
    }

    /**
     * Obtém a URL salva, antes da autenticação.
     *
     * @return A URL, se houver.
     */
    public static Optional<String> getSavedRequest() {
        return getHttpSession().map(s -> (String) s.getAttribute(SecurityFilter.REQUESTED_PATH_KEY));
    }

    public static void run(String message, Logger log, Executable block) {
        try {
            block.execute();
            if (message != null) infoMsg(message);

        } catch (Exception e) {
            processException(e, log);
        }
    }

    private static void processException(Exception e, Logger log) {
        Optional<String> msg = ObjectUtils.unwrap(e);
        if (msg.isPresent()) warnMsg(msg.get());
        else logError("unexpected_error", log, e);
    }

    public static void run(Logger log, Executable block) {
        run(null, log, block);
    }

    public static <T> T call(Logger log, Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            processException(e, log);
        }
        return null;
    }

    /**
     * Fornecedor de lista de entidades. Este método captura qualquer exceção ocorrida na tentativa de gerar
     * a lista de entidades, e armazena uma mensagem de erro JSF, que pode ser apresentada no frontend.
     *
     * @param callable O fornecedor da lista de entidades.
     * @param <T>      O tipo da entidade da lista.
     * @return A lista de entidades.
     */
    public static <T> List<T> list(final Callable<List<T>> callable) {
        try {
            // Invoca o fornecedor.
            return callable.call();

        } catch (Exception e) {
            processException(e, LOG);
        }
        return Collections.emptyList();
    }

    @Nullable
    public static <T> T serializableToObject(@Nullable final String valueStr, @NotNull final Function<Serializable, T> function) {
        try {
            // Se a string contendo o valor foi fornecida, aplica a função de transformação.
            return valueStr != null ? function.apply(valueStr) : null;

        } catch (NumberFormatException e) {
            // Ignora.
        } catch (Exception e) {
            logError("unexpected_error", LOG, e);
        }
        return null;
    }

    @Nullable
    public static <T> T longToObject(final String valueStr, final Function<Long, T> function) {
        try {
            // Se a string contendo o valor foi fornecida, aplica a função de transformação.
            return valueStr != null ? function.apply(Long.decode(valueStr)) : null;

        } catch (NumberFormatException e) {
            // Ignora.
        } catch (Exception e) {
            logError("unexpected_error", LOG, e);
        }
        return null;
    }

    public static <T> T integerToObject(final String valueStr, final Function<Integer, T> function) {
        try {
            // Se a string contendo o valor foi fornecida, aplica a função de transformação.
            return valueStr != null ? function.apply(Integer.decode(valueStr)) : null;

        } catch (NumberFormatException e) {
            // Ignora.
        } catch (Exception e) {
            logError("unexpected_error", LOG, e);
        }
        return null;
    }

    public static String asString(final Callable<Object> callable) {
        try {
            if (callable != null) {
                Object obj = callable.call();
                return obj != null ? obj.toString() : null;
            }

        } catch (Exception e) {
            logError("unexpected_error", LOG, e);
        }
        return null;
    }


    public static <E> String asString(final Object obj, final Function<E, String> function) {
        try {
            return obj != null ? function.apply((E) obj) : null;

        } catch (Exception e) {
            logError("unexpected_error", LOG, e);
        }
        return null;
    }

    public static FacesContext getContext() {
        return FacesContext.getCurrentInstance();
    }

    public static PartialViewContext getPartialViewContext() {
        return getContext().getPartialViewContext();
    }

    public static boolean isAjaxRequest() {
        return getContext().getPartialViewContext().isAjaxRequest();
    }

}
