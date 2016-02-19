package br.com.ttsec.domain.baseentity;

import javax.persistence.*;

@MappedSuperclass
public abstract class BaseEntityLongPKSequence extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_TTSEC")
    @SequenceGenerator(name="SEQ_TTSEC", sequenceName="SEQ_TTSEC")
    @Column(name = "ID")
    protected Long id;

    protected BaseEntityLongPKSequence() {
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
