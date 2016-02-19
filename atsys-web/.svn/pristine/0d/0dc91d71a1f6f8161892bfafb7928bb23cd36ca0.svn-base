package com.sixlabs.atsys.domain;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

/**
 * Contador genérico de ocorrências de um determinado tipo em uma coleção de outro tipo.
 *
 * @param <S> O tipo a ser contado.
 */
public class Counter<S> {

    // O resultado da contagem do tipo S.
    private Map<S, Long> results;

    // A quantidade total da coleção.
    private int total;

    private Counter() {
    }

    /**
     * Retorna um novo contador.
     *
     * @param value      A função que mapeia o tipo T no tipo S.
     * @param collection A coleção de objetos que contém o tipo a ser contado.
     * @param <T>        O objeto que contém o tipo a ser contado.
     * @param <S>        O tipo a ser contado.
     * @return O contador.
     */
    public static <T, S> Counter count(Function<T, S> value, Collection<? extends T> collection) {
        Counter counter = new Counter();
        counter.results = collection.stream().collect(groupingBy(value, counting()));
        counter.total = collection.size();
        return counter;
    }

    /**
     * Obtém a contagem do tipo S.
     *
     * @param value O objeto a ser contado.
     * @return A contagem do objeto.
     */
    public Long getCount(S value) {
        return results.getOrDefault(value, 0L);
    }

    /**
     * Indica se existe ao menos uma ocorrência na contagem do valor.
     *
     * @param value O valor a ser contado.
     * @return true/false.
     */
    public boolean atLeastOne(S value) {
        return getCount(value) > 0L;
    }

    /**
     * Indica se a contagem do valor é igual a quantidade total da coleção.
     *
     * @param value O valor a ser contado.
     * @return true/false.
     */
    public boolean allEquals(S value) {
        return getCount(value) == total;
    }

}
