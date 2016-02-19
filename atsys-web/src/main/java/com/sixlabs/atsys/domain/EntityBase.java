package com.sixlabs.atsys.domain;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * @author averri
 */
@MappedSuperclass
public abstract class EntityBase<ID extends Serializable> implements Serializable {

    public EntityBase() {
    }

    /**
     * Obtém o ID da entidade.
     *
     * @return O ID da entidade.
     */
    public abstract ID getId();

    /**
     * Configura o ID da entidade.
     *
     * @param id O ID da entidade.
     */
    public abstract void setId(ID id);

    /**
     * Informa se a entidade é fresca (nunca foi salva no banco de dados).
     *
     * @return true/false.
     */
    public abstract boolean isFresh();

    private void setFresh(boolean isNew) {
        // Ignora. Este método está aqui somente para o validador do IDE não reclamar que está
        // Faltando o método set da propriedade. Não posso anotar o método 'isNew' com '@Transient'
        // porque não é possível misturar antoações da JPA em métodos e campos.
    }
}
