package com.sixlabs.atsys.web.jsf.converter;

import com.sixlabs.atsys.domain.EnumBase;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import java.util.function.Function;

public abstract class EnumJsfConverter<E extends EnumBase> implements Converter {

    /**
     * Converte de string para o objeto enum.
     *
     * @return A função de conversão.
     */
    public abstract Function<String, E> stringToEnum();

    /**
     * Obtém a classe do enum que este conversor é capaz de tratar.
     *
     * @return A classe do enum.
     */
    public abstract Class<E> getEnumClass();

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return value != null ? stringToEnum().apply(value) : null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value != null ? ((E) value).getCodeStr() : null;
    }
}
