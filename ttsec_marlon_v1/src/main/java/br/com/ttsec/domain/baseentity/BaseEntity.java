package br.com.ttsec.domain.baseentity;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.Transient;
import java.io.Serializable;

public abstract class BaseEntity<ID extends Serializable> implements Serializable {

    // Indica se a entidade já foi salva no banco de dados.
    @Transient
    protected boolean saved;

    public BaseEntity() {
    }

    /**
     * Chamado automaticamente quando a entidade é salva no repositório.
     */
    @PostPersist
    protected void postPersist() {
        // Indica que foi salva.
        saved = true;
    }

    /**
     * Chamado automaticamente quando a entidade foi carregada do repositório.
     */
    @PostLoad
    protected void postLoad() {
        // Indica que já foi salva.
        saved = true;
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
    protected abstract void setId(ID id);

    /**
     * Informa se a entidade é fresca (nunca foi salva no banco de dados).
     *
     * @return true/false.
     */
    public final boolean isFresh() {
        return !saved;
        //return getId() == null;
    }

    private void setFresh(boolean fresh) {
        // Ignora. Este método está aqui somente para o validador do IDE não reclamar que está
        // Faltando o método set da propriedade. Não posso anotar o método 'isNew' com '@Transient'
        // porque não é possível misturar antoações da JPA em métodos e campos.
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEntity)) return false;
        BaseEntity that = (BaseEntity) o;
        return !(getId() != null ? !getId().equals(that.getId()) : that.getId() != null);
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

    /**
     * Obtém o hash code do objeto gerado pelo Java.
     *
     * @return O hash code do objeto.
     */
    public String getObjectHashCode() {
        return this.getClass().getSimpleName() + "@" + System.identityHashCode(this);
    }
}
