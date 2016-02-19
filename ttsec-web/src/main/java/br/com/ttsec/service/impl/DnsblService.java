package br.com.ttsec.service.impl;

import br.com.ttsec.domain.Dnsbl;
import br.com.ttsec.repository.DnsblRepository;
import br.com.ttsec.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.ejb.PostActivate;
import javax.ejb.Stateless;
import javax.inject.Inject;

// TODO: Exercício - implementar melhor a classe BaseService.
@Stateless
public class DnsblService extends BaseService<Dnsbl> {

    private static final Logger LOG = LoggerFactory.getLogger(DnsblService.class);

    // O repositório será injetado sem problemas.
    @Inject
    private DnsblRepository repository;

    // Estas anotações dizem para executar este método quando o EJB for
    // criado pela primeira vez, e também quando for ATIVADO (acordou da hibernação).
    @PostConstruct
    @PostActivate
    public void init() {
        LOG.info("O repositório usado por este bean é: {}", repository);
    }


}
