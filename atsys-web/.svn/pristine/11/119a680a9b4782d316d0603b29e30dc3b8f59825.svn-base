package com.sixlabs.atsys.domain;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EnumCache<E extends EnumBase, K> {

    private final Map cache = new ConcurrentHashMap<>();

    public EnumCache(final Class<E> clazz) {
        if (clazz == null)
            throw new IllegalArgumentException("'clazz' cannot be null.");
        Arrays.asList(clazz.getEnumConstants()).forEach(e -> cache.put(e.getCode(), e));
    }

    public E of(K key) {
        return key != null ? (E) cache.get(key) : null;
    }

}
