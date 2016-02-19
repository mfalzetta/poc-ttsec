package com.sixlabs.atsys.repository;

import com.sixlabs.atsys.domain.EntityBase;
import com.sixlabs.atsys.domain.OccurredWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.event.Event;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author averri
 */
public abstract class RepImpl<E extends EntityBase<ID>, ID extends Serializable> implements Rep<E, ID> {

    private static final Logger LOG = LoggerFactory.getLogger(RepImpl.class);

    @PersistenceContext(unitName = "application-pu")
    protected EntityManager em;

    public abstract Class<E> getEntityClass();

    public Optional<Event<OccurredWith<E>>> getEvent() {
        return Optional.empty();
    }

    public RepImpl() {
    }

    @Override
    public Optional<E> findById(final ID id) {
        return Optional.ofNullable(em.find(getEntityClass(), id));
    }

    @Override
    public E save(final E entity) {
        if (entity.isFresh()) {
            LOG.info("Salvando: '{}'...", entity);
            em.persist(entity);
            getEvent().ifPresent(e -> e.fire(OccurredWith.created(entity)));
            return entity;

        } else {
            LOG.info("Atualizando: '{}'...", entity);
            getEvent().ifPresent(e -> e.fire(OccurredWith.updated(entity)));
            if (!em.contains(entity)) return em.merge(entity);
            else return entity;
        }
    }

    @Override
    public void save(final Collection<E> entities) {
        entities.forEach(this::save);
        flush();
    }

    @Override
    public void remove(final E entity) {
        if (entity.getId() != null) {
            LOG.info("Removendo '{}'...", entity);
            removeById(entity.getId());
            getEvent().ifPresent(e -> e.fire(OccurredWith.removed(entity)));
        }
    }

    @Override
    public void remove(final Collection<E> entidades) {
        entidades.forEach(this::remove);
        flush();
    }

    public final E getReference(ID id) {
        return em.getReference(getEntityClass(), id);
    }

    @Override
    public void removeById(ID id) {
        em.remove(getReference(id));
    }

    /**
     * Query que lista todos as entidades do repositório.
     *
     * @return A query.
     */
    public TypedQuery<E> allEntities() {
        return em.createQuery("SELECT c FROM "
                + getEntityClass().getSimpleName() + "  c", getEntityClass());
    }

    /**
     * Query que lista as entidades do repositório cuja propriedade contenha o valor de pesquisa.
     *
     * @param name  O nome da propriedade da entidade.
     * @param value O valor de pesquisa.
     * @return A query.
     */
    public TypedQuery<E> byPropertyLike(String name, String value) {
        return em.createQuery("SELECT e FROM " + getEntityClass().getSimpleName()
                + " e WHERE e." + name + " LIKE :value ORDER BY e." + name, getEntityClass())
                .setParameter("value", "%" + value + "%");
    }

    public TypedQuery<E> byProperty(String name, Object value) {
        return em.createQuery("SELECT e FROM " + getEntityClass().getSimpleName()
                + " e WHERE e." + name + " = :value", getEntityClass())
                .setParameter("value", value);
    }

    public TypedQuery<E> byPropertyIn(String name, List values) {
        return em.createQuery("SELECT e FROM " + getEntityClass().getSimpleName()
                + " e WHERE e." + name + " in :value", getEntityClass())
                .setParameter("value", values);
    }

    @Override
    //@Transactional(readOnly = true)
    public List<E> listAll() {
        return list(allEntities());
    }

    @Override
    //@Transactional(readOnly = true)
    public List<E> listByProperty(String name, Object value) {
        return list(byProperty(name, value));
    }

    @Override
    //@Transactional(readOnly = true)
    public List<E> listByPropertyLike(String name, String value) {
        return list(byPropertyLike(name, value));
    }

    @Override
    //@Transactional(readOnly = true)
    public List<E> listByPropertyIn(String name, List<Object> values) {
        return list(byPropertyIn(name, values));
    }

    @Override
    public void flush() {
        em.flush();
    }

    /**
     * Retorna um resultado optional do tipo T. Caso não seja encontrado um resultado único,
     * o método vai retornar um opcional vazio.
     *
     * @param query A query.
     * @param <T>   O tipo do objeto retornado pela query.
     * @return Um optional do tipo T.
     */
    protected static <T> Optional<T> singleResult(final TypedQuery<T> query) {
        try {
            return Optional.ofNullable(query.getSingleResult());

        } catch (NoResultException e) {
            return Optional.empty();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retorna uma lista de objetos do tipo T. Nunca retorna null.
     * Se não houverem registros, retorna uma lista vazia.
     *
     * @param query A query.
     * @return A lista de entidades, pode ser vazia.
     */
    protected List<E> list(final TypedQuery<E> query) {
        try {
            final List<E> list = query.getResultList();
            em.clear();
            return list;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
