package br.com.ttsec.repository.impl;

import br.com.ttsec.domain.Dnsbl;
import br.com.ttsec.repository.DnsblRepository;

import javax.ejb.Stateless;

// OBS: Importante marcar a anotação stateless.
// Não armazene variáveis de instância, pois é um EJB stateless!
@Stateless
public class DnsblRepositoryImpl extends RepositoryImpl<Long, Dnsbl>
        implements DnsblRepository {

    @Override
    public Class<Dnsbl> getEntityClass() {
        return Dnsbl.class;
    }
}
