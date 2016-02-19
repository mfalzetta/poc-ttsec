
package br.com.ttsec.repository;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Representa a interface de um repositório de dados do sistema.
 *
 * @author averri
 */
public interface Repository<ID, E> extends Serializable {

    /**
     * Encontra uma entidade.
     *
     * @param id O ID da entidade.
     * @return A entidade.
     */
    E findById(ID id);

    /**
     * Salva uma entidade no repositório.
     *
     * @param entity A entidade a ser salva.
     * @return A entidade salva.
     */
    E save(E entity);

    E update(E entity);

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

    public void flush();

}
