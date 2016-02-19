package com.sixlabs.atsys.domain;

import javax.persistence.*;
import java.util.UUID;

/**
 * @author averri
 */
@MappedSuperclass
public abstract class EntityBaseUUID extends EntityBase<String> {

    @Id
    protected String id;

    // Indica se a entidade já foi salva no banco de dados.
    @Transient
    private boolean fresh;

    protected EntityBaseUUID() {
        super();
        this.id = UUID.randomUUID().toString();
        fresh = true;
    }

    @PostLoad
    private void postLoad() {
        fresh = false;
    }

    @PostPersist
    private void postPersist() {
        fresh = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public final boolean isFresh() {
        return fresh;
    }
}
