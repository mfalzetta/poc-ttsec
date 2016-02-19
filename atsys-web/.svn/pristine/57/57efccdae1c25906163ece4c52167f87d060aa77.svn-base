package com.sixlabs.atsys.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class Enums {

    private static final Map<Class, EnumCache> CACHE = new ConcurrentHashMap<>();

    private Enums() {
    }

    public static <E extends EnumBase, K> E of(Class<E> clazz, K code) {
        if (clazz == null)
            throw new IllegalArgumentException("'clazz' cannot be null.");
        CACHE.putIfAbsent(clazz, new EnumCache(clazz));
        return (E) CACHE.get(clazz).of(code);
    }

    public static <E extends EnumBase> E ofInteger(Class<E> clazz, String code) {
        if (code != null) return of(clazz, Integer.valueOf(code));
        else return null;
    }

    public static <E extends EnumBase> List<E> list(Class<E> clazz) {
        return Arrays.asList(clazz.getEnumConstants());
    }

    public static <E extends EnumBase> Stream<E> stream(Class<E> clazz) {
        return list(clazz).stream();
    }

}
