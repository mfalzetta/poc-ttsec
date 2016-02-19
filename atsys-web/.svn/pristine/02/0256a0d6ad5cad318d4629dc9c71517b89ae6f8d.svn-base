package com.sixlabs.atsys.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("W")
public class WebComplaint extends Complaint {

    @Column(name = "TEXT")
    private String text;

    protected WebComplaint() {
    }

    public WebComplaint(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
