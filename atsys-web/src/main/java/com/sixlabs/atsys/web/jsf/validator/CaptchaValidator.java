package com.sixlabs.atsys.web.jsf.validator;

import com.sixlabs.atsys.web.jsf.util.FacesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;

/**
 * @author averri
 */
@FacesValidator(value = "captcha")
public class CaptchaValidator implements Validator {

    private static final Logger LOG = LoggerFactory.getLogger(CaptchaValidator.class);

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        final HtmlInputText input = (HtmlInputText) component;

        final String userCaptcha = (String) value;

        final HttpSession session = FacesUtils.getHttpSession(true);

        // Valida se o valor do campo é nulo.
        if (userCaptcha == null) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "O campo " + input.getLabel() + " não pode ser nulo.", null));
        }

        // Obtém o texto do captcha gerado, da sessão HTTP.
        final String generatedCaptcha = (String) session.getAttribute("captcha");

        LOG.debug("Captcha gerado: '{}', captcha fornecido: '{}'.", generatedCaptcha, userCaptcha);

        // Se o valor do campo não for igual ao captcha gerado,
        if (!userCaptcha.equals(generatedCaptcha))
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "O texto não confere!", null));
    }

}
