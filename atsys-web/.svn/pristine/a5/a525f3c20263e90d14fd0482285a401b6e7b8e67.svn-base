package com.sixlabs.atsys.domain;

public interface EnumBase<E extends Enum, K> {

    K getCode();

    String getDescription();

    default String getCodeStr() {
        if (getCode() != null) return getCode().toString();
        else return null;
    }

}
