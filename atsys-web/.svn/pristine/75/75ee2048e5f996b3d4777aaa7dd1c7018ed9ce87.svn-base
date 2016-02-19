package com.sixlabs.atsys.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

/**
 * @author averri
 */
@MappedSuperclass
public abstract class EntityTimedID extends EntityBaseID {

    // Data de criação.
    @Column(name = "CREATED", nullable = false, updatable = false)
    private LocalDateTime created;

    // Data de alteração.
    @Column(name = "UPDATED", nullable = false)
    private LocalDateTime updated;

    public EntityTimedID() {
        super();
        LocalDateTime now = LocalDateTime.now();
        this.created = now;
        this.updated = now;
    }

    @PreUpdate
    private void preUpdate() {
        this.updated = LocalDateTime.now();
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

}
