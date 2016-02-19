package com.sixlabs.atsys.web.jsf.converter;

import com.sixlabs.atsys.domain.EntityBaseUUID;
import com.sixlabs.atsys.repository.Rep;
import com.sixlabs.atsys.web.jsf.util.FacesUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public abstract class EntityBaseUUIDJsfConverter<E extends EntityBaseUUID> implements Converter {

    /**
     * Obtém o repositório da entidade.
     *
     * @return O repositório da entidade.
     */
    public abstract Rep<E, String> getRepository();

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String idStr) {
        return FacesUtils.asString(() -> getRepository().findById(idStr).orElse(null));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object obj) {
        if (obj != null) return ((E) obj).getId();
        return null;
    }
}
