package com.sixlabs.atsys.web.jsf.converter;

import com.sixlabs.atsys.domain.utils.ObjectUtils;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.Properties;

@FacesConverter(value = "properties")
public class PropertiesJsfConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            return ObjectUtils.toProperties(value);

        } catch (Exception e) {
            throw new FacesException("Contém valores inválidos.");
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return ObjectUtils.toString((Properties) value);
    }
}
