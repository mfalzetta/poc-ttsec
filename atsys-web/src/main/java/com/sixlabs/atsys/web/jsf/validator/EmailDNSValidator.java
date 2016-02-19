package com.sixlabs.atsys.web.jsf.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.naming.CommunicationException;
import javax.naming.NameNotFoundException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;

/**
 * Verifica a existência do email fornecido.
 *
 * @author averri
 */
@FacesValidator(value = "emailDns")
public class EmailDNSValidator implements Validator {

    private static final Logger LOG = LoggerFactory.getLogger(EmailDNSValidator.class);

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        String email = (String) value;

        // Obtém o índice do caractere arroba.
        int iArroba = email.indexOf("@");

        // Se o nome de domínio existe,
        if (iArroba != -1) {

            // Extrai a parte do domínio do email.
            String dominio = email.substring(iArroba + 1);

            // Faz um lookup no servidor de DNS.
            int lookup = doLookup(dominio);

            // Se o nome de domínio não foi encontrado nos servidores DNS,
            if (lookup == -1) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "O email não é válido.", null));
            }

        } else {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "O email não é válido.", null));
        }
    }

    public static int doLookup(String hostName) {
        try {
            Hashtable env = new Hashtable();
            env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
            //env.put(Context.PROVIDER_URL, "dns://8.8.8.8");
            DirContext ictx = new InitialDirContext(env);
            //Attributes attrs = ictx.getAttributes( hostName, new String[]{"A", "NS", "CNAME", "SOA", "PTR", "MX", "TXT", "HINFO", "AAAA", "NAPTR", "SRV"} );
            Attributes attrs = ictx.getAttributes(hostName, new String[]{"MX"});
            Attribute attr = attrs.get("MX");
            if (attr == null) {
                return 0;
            }
            return attr.size();

        } catch (NameNotFoundException e) {
            // Ignora.
        } catch (CommunicationException e) {
            LOG.warn("Falha ao consultar o DNS.", e);
        } catch (Exception e) {
            LOG.error("Erro inexperado.", e);
        }
        return -1;
    }

}
