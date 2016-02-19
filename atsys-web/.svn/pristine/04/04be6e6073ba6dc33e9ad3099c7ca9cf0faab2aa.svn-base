package com.sixlabs.atsys.domain;

import javax.persistence.*;

@Entity
@DiscriminatorValue("E")
public class EmailComplaint extends Complaint {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "EMAIL_ID")
    private Email email;

    protected EmailComplaint() {
        super();
    }

    public EmailComplaint(Email email) {
        this();
        if (email == null) {
            throw new IllegalArgumentException("'email' cannot be null.");
        }
        this.email = email;
    }

    public Email getEmail() {
        return email;
    }
}
