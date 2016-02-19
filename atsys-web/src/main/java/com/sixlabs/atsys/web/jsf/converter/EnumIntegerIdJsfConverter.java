package com.sixlabs.atsys.web.jsf.converter;

import com.sixlabs.atsys.domain.EnumBase;
import com.sixlabs.atsys.domain.Enums;

import java.util.function.Function;

public abstract class EnumIntegerIdJsfConverter<E extends EnumBase> extends EnumJsfConverter<E> {

    @Override
    public Function<String, E> stringToEnum() {
        return value -> Enums.ofInteger(getEnumClass(), value);
    }
}
