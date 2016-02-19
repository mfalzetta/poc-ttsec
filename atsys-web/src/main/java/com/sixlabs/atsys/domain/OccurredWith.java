package com.sixlabs.atsys.domain;

import org.jetbrains.annotations.NotNull;

/**
 * Representa um evento de alteração ocorrido com uma entidade.
 *
 * @param <E> O tipo da entidade.
 */
public class OccurredWith<E extends EntityBase> {

    public enum Type {CREATED, UPDATED, REMOVED}

    // O tipo da alteração.
    private final Type type;

    // A entidade associada.
    private final E entity;

    public OccurredWith(Type type, E entity) {
        if (type == null) {
            throw new IllegalArgumentException("'type' não pode ser nulo.");
        }
        if (entity == null) {
            throw new IllegalArgumentException("'entity' não pode ser nulo.");
        }
        this.type = type;
        this.entity = entity;
    }

    public static <E extends EntityBase> OccurredWith updated(E entity) {
        return new OccurredWith(Type.UPDATED, entity);
    }

    public static <E extends EntityBase> OccurredWith created(E entity) {
        return new OccurredWith(Type.CREATED, entity);
    }

    public static <E extends EntityBase> OccurredWith removed(E entity) {
        return new OccurredWith(Type.REMOVED, entity);
    }

    @NotNull
    public Type getType() {
        return type;
    }

    @NotNull
    public E getEntity() {
        return entity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OccurredWith<?> that = (OccurredWith<?>) o;
        if (type != that.type) return false;
        return entity.equals(that.entity);
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + entity.hashCode();
        return result;
    }
}
