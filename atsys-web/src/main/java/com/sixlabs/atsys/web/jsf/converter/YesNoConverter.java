package com.sixlabs.atsys.web.jsf.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Alexandre Verri
 */
@FacesConverter(value="yesNo")
public class YesNoConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if ("Sim".equalsIgnoreCase(value)) {
            return Boolean.TRUE;
        } else if ("Não".equalsIgnoreCase(value)) {
            return Boolean.FALSE;
        } else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if(value == null) {
            return null;
        }
        Boolean valor = null;
        try {
            valor = (Boolean) value;
        } catch (Exception e) {
            // Permite usar o conversor no seguinte contexto:
            // <h:outputText id="texto" value="#{empty cc.attrs.value ? 'Sem informações' : cc.attrs.value}" />
            return "Sem informações";
        }
        if (valor) {
            return "Sim";
        } else {
            return "Não";
        }
    }

}

