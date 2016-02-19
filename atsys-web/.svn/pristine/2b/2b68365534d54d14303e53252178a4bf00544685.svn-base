package com.sixlabs.atsys.web.jsf.converter;

import com.sixlabs.atsys.domain.EntityBaseID;
import com.sixlabs.atsys.repository.Rep;
import com.sixlabs.atsys.web.jsf.util.FacesUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public abstract class EntityBaseIdJsfConverter<E extends EntityBaseID> implements Converter {

    /**
     * Obtém o repositório da entidade.
     *
     * @return O repositório da entidade.
     */
    public abstract Rep<E, Long> getRepository();

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String idStr) {
        return FacesUtils.longToObject(idStr, id -> getRepository().findById(id).orElse(null));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object obj) {
        return FacesUtils.asString(((E) obj)::getId);
    }
}
