package com.sixlabs.atsys.repository.converter;

import com.sixlabs.atsys.domain.EnumBase;
import com.sixlabs.atsys.domain.Enums;

import javax.persistence.AttributeConverter;

public abstract class EnumJpaConverter<E extends EnumBase, K>
        implements AttributeConverter<E, K> {

    /**
     * Obtém a classe do enum conhecida por este conversor.
     *
     * @return A classe do enum.
     */
    public abstract Class<E> getEnumClass();

    @Override
    public K convertToDatabaseColumn(E object) {
        return object != null ? (K) object.getCode() : null;
    }

    @Override
    public E convertToEntityAttribute(K code) {
        return code != null ? Enums.of(getEnumClass(), code) : null;
    }
}
