package br.com.ttsec.domain.baseentity;

import javax.persistence.*;

/**
 * @author averri
 */
@MappedSuperclass
public abstract class BaseEntityLongPKIdentity extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    protected Long id;

    protected BaseEntityLongPKIdentity() {
        super();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    protected void setId(Long id) {
        this.id = id;
    }

}
