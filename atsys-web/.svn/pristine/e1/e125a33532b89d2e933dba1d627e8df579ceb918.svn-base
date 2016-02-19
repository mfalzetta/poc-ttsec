package com.sixlabs.atsys.web.jsf.converter;

import com.sixlabs.atsys.domain.Email;
import com.sixlabs.atsys.repository.EmailRep;
import com.sixlabs.atsys.repository.Rep;

import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@FacesConverter(value = "email")
public class EmailJsfConverter extends EntityBaseUUIDJsfConverter<Email> {

    @Inject
    private EmailRep emailRep;

    @Override
    public Rep<Email, String> getRepository() {
        return emailRep;
    }
}
