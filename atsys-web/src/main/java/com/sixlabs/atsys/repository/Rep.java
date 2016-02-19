package com.sixlabs.atsys.repository;

import com.sixlabs.atsys.domain.ApplicationException;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author averri
 */
public interface Rep<E, ID extends Serializable> extends Serializable {

    default <T> Optional<T> singleResult(final Supplier<T> block) {
        try {
            return Optional.ofNullable(block.get());

        } catch (NoResultException | NonUniqueResultException e) {
            return Optional.empty();

        } catch (Exception e) {
            throw new ApplicationException(e);
        }
    }

    /**
     * Encontra uma entidade.
     *
     * @param id O ID da entidade.
     * @return A entidade.
     */
    Optional<E> findById(ID id);

    /**
     * Salva uma entidade no repositório.
     *
     * @param entity A entidade a ser salva.
     * @return A entidade salva.
     */
    E save(E entity);

    /**
     * Salva uma coleção de entidades no repositório.
     *
     * @param entities A coleção de entidades a serem salvas.
     */
    void save(Collection<E> entities);

    /**
     * Remove uma entidade do repositório.
     *
     * @param entity A entidade a ser removida.
     */
    void remove(E entity);

    /**
     * Remove uma coleção de entidades do repositório.
     *
     * @param entities A coleção de entidades a serem removidas.
     */
    void remove(Collection<E> entities);

    /**
     * Remove uma entidade do respositório.
     *
     * @param id O ID da entidade a ser removida.
     */
    void removeById(ID id);

    /**
     * Lista todas as entidades do repositório.
     *
     * @return A lista de entidades.
     */
    List<E> listAll();

    /**
     * Lista entidades.
     *
     * @param name  O nome da propriedade da entidade.
     * @param value O valor da propriedade da entidade.
     * @return A lista de entidades.
     */
    public List<E> listByProperty(String name, Object value);

    //@Transactional(readOnly = true)
    List<E> listByPropertyLike(String name, String value);

    //@Transactional(readOnly = true)
    List<E> listByPropertyIn(String name, List<Object> values);

    public void flush();
}
