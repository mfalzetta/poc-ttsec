package com.sixlabs.atsys.domain;

import javax.persistence.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author averri
 */
@MappedSuperclass
public abstract class EntityBaseID extends EntityBase<Long> {

    private static final AtomicLong idGen = new AtomicLong();

    // ID transitório, utilizado enquanto a entidade não foi salva no banco de dados.
    @Transient
    private Long idTemp;

    // O ID da entidade, após ser salvo no banco de dados.
    // Enquanto não for salvo no banco de dados o valor de id é null.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected EntityBaseID() {
        idTemp = idGen.decrementAndGet();
    }

    public Long getIdTemp() {
        return idTemp;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public final boolean isFresh() {
        return id == null;
    }

    @Override
    public boolean equals(Object o) {
        return getId() == null ? equalsIdTemp(o) : equalsId(o);
    }

    @Override
    public int hashCode() {
        return getId() == null ? hashCodeIdTemp() : hashCodeId();
    }

    private boolean equalsIdTemp(Object o) {
        if (this == o) return true;
        if (!(o instanceof EntityBaseID)) return false;
        EntityBaseID that = (EntityBaseID) o;
        if (!idTemp.equals(that.idTemp)) return false;
        return true;
    }

    private int hashCodeIdTemp() {
        return idTemp.hashCode();
    }

    private boolean equalsId(Object o) {
        if (this == o) return true;
        if (!(o instanceof EntityBaseID)) return false;
        EntityBaseID that = (EntityBaseID) o;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return true;
    }

    private int hashCodeId() {
        return id != null ? id.hashCode() : 0;
    }
}
