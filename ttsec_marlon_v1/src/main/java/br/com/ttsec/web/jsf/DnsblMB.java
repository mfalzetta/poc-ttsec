package br.com.ttsec.web.jsf;

import br.com.ttsec.domain.Dnsbl;
import br.com.ttsec.repository.DnsblRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

// TODO: Exercício - alterar o AbstractMB para usar serviços ao invés de repositórios.
@ManagedBean
@ViewScoped
public class DnsblMB extends AbstractMB<Dnsbl> {

    private static final Logger LOG = LoggerFactory.getLogger(DnsblMB.class);

    // Injeta o repositório.
    // Poderia injetar o seu serviço, se quiser uma camada adicional.
    @Inject
    private DnsblRepository repository;

    // TODO: Ative a injeção abaixo, vai funcionar normalmente.
    //@Inject
    //private DnsblService service;

    // Este método é chamado toda vez que o bean JSF for CRIADO.
    @Override
    public void init() {
        super.init();
        LOG.info("O repositório injetado é: {}", repository);
    }

    @Override
    public DnsblRepository getRepository() {
        // Retorna o repositório, pois é usado pelo AbstractMB para realizar as operações.
        return repository;
    }

    // OBS: Este método é responsável por criar instâncias novas da entidade.
    // Na interface, deve have um botão mapeado para a ação "createAction()" deste managed bean.
    @Override
    public Dnsbl create() {
        // Cria uma instância nova, vazia, ou de acordo com o seu negócio.
        return new Dnsbl();
    }
}
