package com.sixlabs.atsys.web.jsf;

import com.sixlabs.atsys.domain.utils.ObjectUtils;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator(value = "properties")
public class PropertiesValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        try {
            ObjectUtils.toProperties((String) value);

        } catch (Exception e) {
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Contém valores inválidos.", null));
        }
    }
}
