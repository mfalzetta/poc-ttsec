package br.com.ttsec.domain;

import br.com.ttsec.domain.baseentity.BaseEntityLongPKSequence;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "DNSBL")
public class Dnsbl extends BaseEntityLongPKSequence {

    @Column(name = "URI")
    @Pattern(regexp = "^http://.*$", message = "Deve começar com 'http://'!")
    private String uri;

    // Construtor padrao
    public Dnsbl(){
        super();
    }

    // Construtor facilitador
    public Dnsbl(Long id, String uri){
        this.id = id;
        this.uri = uri;
    }

    //@Basic
    ///@Column(name = "URI", nullable = false, insertable = true, updatable = true, length = 100)
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "Dnsbl{" +
                "uri='" + uri + '\'' +
                '}';
    }
}
