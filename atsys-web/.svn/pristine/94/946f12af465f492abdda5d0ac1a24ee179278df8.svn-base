package com.sixlabs.atsys.web.jsf.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@FacesConverter(value = "localDateTime")
public class LocalDateTimeJsfConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null) {
            return LocalDateTime.parse(value, getDateTimeFormatter(component));
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value != null) return getDateTimeFormatter(component).format((LocalDateTime) value);
        return null;
    }

    private DateTimeFormatter getDateTimeFormatter(UIComponent component) {
        final String pattern = (String) component.getAttributes().get("pattern");
        return DateTimeFormatter.ofPattern(pattern != null ? pattern : "dd/MM/yyyy HH:mm");
    }

}
