
package br.com.ttsec.web.jsf;

/**
 * @author averri
 */
public interface CrudAction {

    /**
     * Cria uma nova entidade.
     */
    void createAction();

    /**
     * Salva a entidade em edição.
     */
    void saveAction();

    /**
     * Cancela a ação atual.
     */
    void cancelAction();

    /**
     * Remove a entidade em edição.
     */
    void removeAction();

    /**
     * Obtém o modo atual (inserção, atualização, etc).
     *
     * @return O modo atual de operação.
     */
    Mode getMode();

}
