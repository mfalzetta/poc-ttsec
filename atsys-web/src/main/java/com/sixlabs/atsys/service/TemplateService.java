package com.sixlabs.atsys.service;


import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Map;

import static com.sixlabs.atsys.domain.utils.ObjectUtils.run;

/**
 * Serviço de templates de texto.
 */
@Singleton
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class TemplateService implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(TemplateService.class);

    private VelocityEngine engine;

    @PostConstruct
    public void init() {
        LOG.info("Iniciando serviço de templates...");
        run(LOG, () -> {
            engine = new VelocityEngine();
            engine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
            engine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
            engine.setProperty("input.encoding", "UTF-8");
            engine.setProperty("output.encoding", "UTF-8");
            engine.init();
        });
    }

    /**
     * Retorna o texto resultante da aplicação do modelo ao template.
     *
     * @param templateName O nome do template.
     * @param model        O modelo.
     * @return O texto.
     */
    public String getText(String templateName, Map<String, Object> model) {
        // Obtém o template.
        final Template template = engine.getTemplate(templateName);
        // Cria o contexto do velocity.
        final VelocityContext context = new VelocityContext(model);
        final StringWriter writer = new StringWriter();
        template.merge(context, writer);
        return writer.toString();
    }

}
