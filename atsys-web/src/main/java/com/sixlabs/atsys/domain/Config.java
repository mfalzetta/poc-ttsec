package com.sixlabs.atsys.domain;

import com.sixlabs.atsys.domain.utils.ObjectUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.io.File;
import java.io.Serializable;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.prefs.Preferences;

/**
 * Propriedades da aplicação.
 *
 * @author averri
 */
@Startup
@Singleton
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class Config implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(Config.class);

    // Diretório base.
    private String rootDirPath;

    // IP do media server.
    private String theme;

    // URL para onde será direcionado após o logon.
    private String afterLogonURL;

    // A URL para troca de senha.
    private String passwordChangeURL;

    // Informa se é para usar captcha ou não.
    private Boolean useCaptcha;

    private Boolean useOpentok;

    private int opentokApiKey;

    private String opentokApiSecret;

    private String emailUser;

    private String emailPassword;

    private String emailFrom;

    private String emailExtra;

    protected Config() {
    }

    @PostConstruct
    public void init() {

        LOG.info("Carregando propriedades da aplicação...");

        // Obtém o arquivo de preferências.
        Preferences pref = Preferences.userNodeForPackage(Config.class);

        rootDirPath = pref.get("rootDirPath", "/home/atsys");
        theme = pref.get("theme", "bootstrap");
        afterLogonURL = pref.get("afterLogonURL", "/secured/home.jsf");
        useCaptcha = pref.getBoolean("useCaptcha", true);
        passwordChangeURL = pref.get("passwordChangeURL", "/secured/change-password.jsf");
        useOpentok = pref.getBoolean("useOpentok", true);
        opentokApiKey = pref.getInt("opentokApiKey", 45207292);
        opentokApiSecret = pref.get("opentokApiSecret", "e998bd91d7ae5bb0e10b27562761a577adeba072");

        emailUser = pref.get("emailUser", "contato@malvimadvogados.com.br");
        emailPassword = pref.get("emailPassword", "contato2010!");
        emailFrom = pref.get("emailFrom", "Upsale <alexandreverri@gmail.com>");
        emailExtra = pref.get("emailExtra", "# Recebimento de emails.\n" +
                "mail.store.protocol = pop3s\n" +
                "mail.pop3s.host = pop.gmail.com\n" +
                "mail.pop3s.port = 995\n" +
                "mail.pop3s.auth = true\n" +
                "\n" +
                "# Envio de emails.\n" +
                "mail.transport.protocol = smtps\n" +
                "mail.smtps.host = smtp.gmail.com\n" +
                "mail.smtps.port = 465\n" +
                "mail.smtps.auth = true");

        File root = getRootDir();
        // Se a pasta raiz não existe,
        if (!root.exists()) {
            // Caso não consiga criar o diretório raiz,
            if (!root.mkdirs()) {
                throw new ApplicationException("Não foi possível criar a pasta raiz em '" + rootDirPath + "'.");
            }
        }
    }

    private File getRootDir() {
        return new File(getRootDirPath());
    }

    public void update(Config copy) {

        try {
            // Copia as propriedades para esta instância.
            BeanUtils.copyProperties(this, copy);

            final Preferences pref = Preferences.userNodeForPackage(Config.class);
            pref.put("rootDirPath", rootDirPath);
            pref.put("theme", theme);
            pref.put("urlAfterLogon", afterLogonURL);
            pref.putBoolean("useCaptcha", useCaptcha);
            pref.put("passwordChangeURL", passwordChangeURL);
            pref.putBoolean("useOpentok", useOpentok);
            pref.putInt("opentokApiKey", opentokApiKey);
            pref.put("opentokApiSecret", opentokApiSecret);

            pref.put("emailUser", emailUser);
            pref.put("emailPassword", emailPassword);
            pref.put("emailFrom", emailFrom);
            pref.put("emailExtra", emailExtra);

            pref.flush();

        } catch (Exception e) {
            throw new RuntimeException("Falha ao atualizar as configurações.", e);
        }
    }

    /**
     * Retorna uma cópia do objeto.
     *
     * @return A cópia.
     */
    public Config copy() {
        return ObjectUtils.copy(this);
    }

    /**
     * Carrega as propriedades de configurações default.
     */
    public void reloadDefault() {
        try {
            // Obtém o arquivo de preferências.
            Preferences pref = Preferences.userRoot();
            // Limpa as propriedades configuradas.
            pref.clear();
            // Configura as propriedades.
            init();

        } catch (Exception e) {
            LOG.error("Erro ao limpar as propriedades de configurações.", e);
        }
    }

    /**
     * Obtém o endereço IP do nome de domínio.
     *
     * @return
     */
    public String findDomainNameIp(String domainName) {
        try {
            return Inet4Address.getByName(domainName).getHostAddress();

        } catch (UnknownHostException e) {
            LOG.error("Falha ao obter o IP do nome de domínio.");
        }
        return null;
    }

    public String getMediaDirPath() {
        return getRootDirPath() + "/media";
    }

    public String getRootDirPath() {
        return rootDirPath;
    }

    public void setRootDirPath(String rootDirPath) {
        this.rootDirPath = rootDirPath;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getAfterLogonURL() {
        return afterLogonURL;
    }

    public void setAfterLogonURL(String afterLogonURL) {
        this.afterLogonURL = afterLogonURL;
    }

    public String getPasswordChangeURL() {
        return passwordChangeURL;
    }

    public void setPasswordChangeURL(String passwordChangeURL) {
        this.passwordChangeURL = passwordChangeURL;
    }

    public Boolean getUseCaptcha() {
        return useCaptcha;
    }

    public void setUseCaptcha(Boolean useCaptcha) {
        this.useCaptcha = useCaptcha;
    }

    public String getEmailFrom() {
        return emailFrom;
    }

    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }

    public Boolean getUseOpentok() {
        return useOpentok;
    }

    public void setUseOpentok(Boolean useOpentok) {
        this.useOpentok = useOpentok;
    }

    public int getOpentokApiKey() {
        return opentokApiKey;
    }

    public void setOpentokApiKey(int opentokApiKey) {
        this.opentokApiKey = opentokApiKey;
    }

    public String getOpentokApiSecret() {
        return opentokApiSecret;
    }

    public void setOpentokApiSecret(String opentokApiSecret) {
        this.opentokApiSecret = opentokApiSecret;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getEmailPassword() {
        return emailPassword;
    }

    public void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
    }

    public Properties getEmailProperties() {
        try {
            return ObjectUtils.toProperties(emailExtra);

        } catch (Exception e) {
            throw new IllegalArgumentException("Valores de propriedade inválidos.");
        }
    }

    public String getEmailExtra() {
        return emailExtra;
    }

    public void setEmailExtra(String emailExtra) {
        this.emailExtra = emailExtra;
    }
}
