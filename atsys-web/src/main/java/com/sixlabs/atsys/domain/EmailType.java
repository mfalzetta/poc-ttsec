package com.sixlabs.atsys.domain;

public enum EmailType implements EnumBase<EmailType, Integer> {

    NEW_ACCOUNT(0, "new-account.vm", "Nova conta"),
    RESTORE_ACCOUNT(1, "restore-account.vm", "Recuperação de conta"),
    EMAIL_TEST(2, "email-test.vm", "Teste de envio");

    private Integer code;

    private String template;

    private String description;

    EmailType(Integer code, String template, String description) {
        this.code = code;
        this.template = template;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getTemplate() {
        return template;
    }

    public String getDescription() {
        return description;
    }
}
