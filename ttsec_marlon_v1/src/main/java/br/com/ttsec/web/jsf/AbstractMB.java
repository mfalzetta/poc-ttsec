package br.com.ttsec.web.jsf;

import br.com.ttsec.domain.ApplicationException;
import br.com.ttsec.repository.Repository;
import br.com.ttsec.util.Xerox;
import br.com.ttsec.web.jsf.util.FacesUtils;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author averri
 */
public abstract class AbstractMB<E> implements CrudOperation<E>, CrudAction, Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractMB.class);

    private int first;

    // A entidade em edição.
    protected E entity;

    // A lista de entidades.
    protected List<E> list;

    // O modo atual de interação (inserção, edição, leitura).
    protected Mode mode;

    // Filtros usados na interface (ex: filtos de colunas nas tabelas do Primefaces).
    private Map<String, String> filter;

    public AbstractMB() {
        mode = Mode.READ;
        filter = new HashMap<>();
        list = new ArrayList<>();
    }

    /**
     * Inicialização.
     */
    @PostConstruct
    public void init() {
        LOG.info("Iniciando...");
        list = getRepository().listAll();
    }

    /**
     * Obtém o repositório da entidade.
     *
     * @return O repositório da entidade.
     */
    public abstract Repository<? extends Serializable, E> getRepository();

    /**
     * Obtém a entidade em edição.
     *
     * @return A entidade em edição.
     */
    public E getEntity() {
        return entity;
    }

    /**
     * Configura a entidade.
     *
     * @param entity A entidade a ser salva.
     */
    public void setEntity(E entity) {
        // Faz uma cópia para não alterar o objeto original.
        this.entity = Xerox.copy(entity);
    }

    /**
     * Obtém a lista de entidades.
     *
     * @return A lista de entidades.
     */
    public List<E> getList() {
        return list;
    }

    @Override
    public Mode getMode() {
        return mode;
    }

    public void onSelect(SelectEvent event) {
        //entity = Xerox.copy((E) event.getObject());
        onEdit();
    }

    public void onEdit() {
        mode = Mode.UPDATE;
        postSelect();
    }

    public void postSelect() {
    }

    /**
     * Cria uma nova entidade para edição.
     */
    @Override
    public void createAction() {
        try {
            entity = create();
            mode = Mode.INSERT;
            postCreate();

        } catch (Exception e) {
            LOG.error("Erro ao criar nova entidade.", e);
            FacesUtils.addErrorMessage("Desculpe, houve um erro inesperado.");
        }
    }

    @Override
    public void postCreate() {
        LOG.debug("Entidade '{}' criada.", entity);
    }

    /**
     * Salva a entidade em edição.
     */
    @Override
    public void saveAction() {
        try {
            preSave();
            save();
            postSave();

        } catch (ApplicationException e) {
            FacesUtils.addErrorMessage(e.getMessage());

        } catch (Exception e) {
            LOG.error("Erro ao salvar.", e);
            FacesUtils.addErrorMessage("Desculpe, houve um erro inesperado.");
        }
    }

    @Override
    public void preSave() {
        LOG.debug("Pre-salvamento da entidade '{}'.", entity);
    }

    @Override
    public void save() {
        LOG.info("Salvando a entidade '{}'.", entity);
        // Salva a entidade no repositório.
        getRepository().save(entity);
        // Obtém o índice da entidade na lista.
        int idx = list.indexOf(entity);
        // Se a entidade não existir na lista,
        if (idx == -1) {
            // Adiciona à lista.
            list.add(entity);
        } else {
            // Atualiza a lista.
            list.set(idx, entity);
        }
    }

    @Override
    public void postSave() {
        String msg = "Salvo com sucesso.";
        FacesUtils.addInfoMessage(msg);
        LOG.debug(msg);
        // Configura o modo update.
        mode = Mode.UPDATE;
    }

    /**
     * Cancela a ação atual.
     */
    @Override
    public void cancelAction() {
        entity = null;
        String msg = "Ação cancelada.";
        FacesUtils.addInfoMessage(msg);
        LOG.debug(msg);
        // Configura o modo leitura.
        mode = Mode.READ;
    }

    @Override
    public void cancel() {
    }

    /**
     * Remove a entidade em edição.
     */
    @Override
    public final void removeAction() {
        try {
            preRemove();
            remove();
            postRemove();

        } catch (RuntimeException e) {
            //LOG.error("Erro ao remover.", e);
            FacesUtils.addWarnMessage("Não foi possível excluir porque existem dados associados.");

        } catch (Exception e) {
            LOG.error("Erro ao remover.", e);
            FacesUtils.addErrorMessage("Desculpe, houve um erro inesperado.");
        }
    }

    @Override
    public void preRemove() {
    }

    @Override
    public void remove() {
        E removed = entity;
        getRepository().remove(entity);
        list.remove(removed);
        entity = null;
    }

    @Override
    public void postRemove() {
        String msg = "Removido com sucesso.";
        FacesUtils.addInfoMessage(msg);
        LOG.debug(msg);
        mode = Mode.READ;
    }

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public Map<String, String> getFilter() {
        return filter;
    }
}
