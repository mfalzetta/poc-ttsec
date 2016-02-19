package br.com.ttsec.repository.impl;

import br.com.ttsec.domain.baseentity.BaseEntity;
import br.com.ttsec.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Representa a implementação padrão para acesso a um repositório do sistema.
 *
 * @author averri
 */
public abstract class RepositoryImpl<ID extends Serializable, E extends BaseEntity<ID>>
        implements Repository<ID, E> {

    private static final Logger LOG = LoggerFactory.getLogger(RepositoryImpl.class);

    // O gerenciador de entidades da JPA.
    @PersistenceContext(unitName = "application-pu")
    protected EntityManager em;

    // Obtém a classe da entidade gerenciada.
    public abstract Class<E> getEntityClass();

    @Override
    public E findById(final ID id) {
        return em.find(getEntityClass(), id);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override
    public E save(final E entity) {

        // Se a entidade for nova,
        if (entity.isFresh()) {
            LOG.debug("Salvando nova entidade: '{}'.", entity);
            em.persist(entity);
            em.flush();
            return entity;

        } else {
            LOG.debug("Atualizando entidade: '{}'.", entity);
            if (!em.contains(entity)) return update(entity);
            else return entity;
        }
    }

    @Override
    public E update(final E entity) {
        final E merged = em.merge(entity);
        em.flush();
        return merged;
    }

    @Override
    public void save(final Collection<E> entities) {
        // Para cada entidade,
        for (final E entity : entities) {
            save(entity);
        }
        //em.flush();
    }

    @Override
    public void remove(final E entity) {
        if (entity.getId() != null) {
            final E merged = em.merge(entity);
            em.remove(merged);
//            removeById(entity.getId());
        } else LOG.warn("Tentativa de remoção de uma entidade sem ID.");
    }

    @Override
    public void remove(final Collection<E> entidades) {
        for (E e : entidades) {
            remove(e);
        }
        em.flush();
    }

    public final E getReferencia(ID id) {
        return em.getReference(getEntityClass(), id);
    }

    @Override
    public void removeById(ID id) {
        em.remove(getReferencia(id));
    }

    @Override
    public List<E> listAll() {
        List<E> list = em.createQuery("SELECT c FROM "
                + getEntityClass().getSimpleName() + " AS c", getEntityClass()).getResultList();
        em.clear();
        return list == null ? new ArrayList<E>() : list;
    }

    @Override
    public List<E> listByProperty(String name, Object property) {
        List<E> lista = em.createQuery("SELECT e FROM "
                + getEntityClass().getSimpleName() + " e WHERE e." + name + " = :prop", getEntityClass())
                .setParameter("prop", property).getResultList();
        em.clear();
        return (lista == null) ? new ArrayList<E>() : lista;
    }

    @Override
    public void flush() {
        em.flush();
    }
}
