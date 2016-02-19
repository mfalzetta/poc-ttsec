package com.sixlabs.atsys.web.jsf.model;

/**
 * @author averri
 */
public interface CrudOperation<E> {

    /**
     * Cria uma nova entidade.
     *
     * @return A entidade criada.
     */
    E create();

    /**
     * Operação de pós-criação da entidade.
     */
    void postCreate();

    /**
     * Operação de pré-salvamento.
     */
    void preSave();

    /**
     * Operação de salvamento da entidade.
     */
    void save();

    /**
     * Operação de pós-salvamento da entidade.
     */
    void postSave();

    /**
     * Cancela a operação atual.
     */
    void cancel();

    /**
     * Operação de pré-remoção da entidade.
     */
    void preRemove();

    /**
     * Operação de remoção da entidade.
     */
    void remove();

    /**
     * Operação de pós-remoção da entidade.
     */
    void postRemove();

}
