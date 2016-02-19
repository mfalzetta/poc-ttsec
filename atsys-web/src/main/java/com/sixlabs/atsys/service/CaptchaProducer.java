package com.sixlabs.atsys.service;

import com.sixlabs.atsys.domain.Captcha;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

import javax.enterprise.inject.Produces;
import java.util.Properties;

/**
 * Fábrica de produtores de captchas.
 */
public class CaptchaProducer {

    @Produces
    @Captcha
    public Producer create() {
        final Properties props = new Properties();
        props.put("kaptcha.border", "no");
        props.put("kaptcha.textproducer.font.color", "100,100,100");
        props.put("kaptcha.noise.color", "100,100,100");
        final DefaultKaptcha captcha = new DefaultKaptcha();
        captcha.setConfig(new Config(props));
        return captcha;
    }

}
