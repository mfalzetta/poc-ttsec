package com.sixlabs.atsys.repository;


import com.sixlabs.atsys.domain.Email;

import javax.ejb.Stateless;

@Stateless
public class EmailRepImpl extends RepImpl<Email, String> implements EmailRep {

    @Override
    public Class<Email> getEntityClass() {
        return Email.class;
    }
}
