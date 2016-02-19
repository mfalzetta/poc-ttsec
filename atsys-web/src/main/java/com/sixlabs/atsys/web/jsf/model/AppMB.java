package com.sixlabs.atsys.web.jsf.model;

import com.sixlabs.atsys.domain.Config;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import java.io.Serializable;

@ManagedBean
@ApplicationScoped
public class AppMB implements Serializable {

    @Inject
    private Config config;

    public AppMB() {
    }
}
