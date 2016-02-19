package com.sixlabs.atsys.web.jsf.model;

import com.sixlabs.atsys.domain.ApplicationException;
import com.sixlabs.atsys.domain.EntityBase;
import com.sixlabs.atsys.repository.Rep;
import com.sixlabs.atsys.domain.utils.ObjectUtils;
import com.sixlabs.atsys.web.jsf.util.FacesUtils;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.sixlabs.atsys.web.jsf.util.FacesUtils.run;

/**
 * @author averri
 */
public abstract class AbstractMB<E extends EntityBase>
        implements CrudOperation<E>, CrudAction, Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractMB.class);

    // A entidade em edição.
    protected E entity;

    // A lista de entidades.
    // TODO: alterar para private.
    protected List<E> list;

    // O modo atual de interação (inserção, edição, leitura).
    protected Mode mode;

    protected AbstractMB() {
        this.list = new ArrayList<>();
        this.mode = Mode.READ;
    }

    @PostConstruct
    private void postContruct() {
        init();
    }

    public static InputStream getInputStream(UploadedFile file) {
        try {
            return file.getInputstream();
        } catch (Exception e) {
            throw new ApplicationException("Falha ao ler o arquivo.");
        }
    }

    /**
     * Inicialização.
     */
    public void init() {
        run(LOG, () -> list = getRepository().listAll());
    }

    /**
     * Obtém o repositório da entidade.
     *
     * @return O repositório da entidade.
     */
    public abstract <ID extends Serializable> Rep<E, ID> getRepository();

    /**
     * Obtém a entidade em edição.
     *
     * @return A entidade em edição.
     */
    public E getEntity() {
        return entity;
    }

    /**
     * Obtém a lista de entidades.
     *
     * @return A lista de entidades.
     */
    public List<E> getList() {
        return list;
    }

    protected void setList(List<E> list) {
        this.list = list;
    }

    /**
     * Configura a entidade.
     *
     * @param entity A entidade a ser salva.
     */
    public void setEntity(E entity) {
        this.entity = ObjectUtils.copy(entity);
    }

    @Override
    public Mode getMode() {
        return mode;
    }

    public void onSelect(SelectEvent event) {
        entity = ObjectUtils.copy((E) event.getObject());
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
        run(LOG, () -> {
            entity = create();
            mode = Mode.INSERT;
            postCreate();
        });
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
        run(LOG, () -> {
            preSave();
            save();
            postSave();
        });
    }

    @Override
    public void preSave() {
        LOG.debug("Pre-salvamento da entidade '{}'.", entity);
    }

    @Override
    public void save() {
        LOG.debug("Salvando a entidade '{}'.", entity);
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
        FacesUtils.infoMsg(msg);
        LOG.debug(msg);
        mode = Mode.UPDATE;
    }

    /**
     * Cancela a ação atual.
     */
    @Override
    public void cancelAction() {
        entity = null;
        String msg = "Ação cancelada.";
        FacesUtils.infoMsg(msg);
        LOG.debug(msg);
        mode = Mode.READ;
    }

    @Override
    public void cancel() {
    }

    /**
     * Remove a entidade em edição.
     */
    @Override
    public void removeAction() {
        run(LOG, () -> {
            preRemove();
            remove();
            postRemove();
        });
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
        FacesUtils.infoMsg(msg);
        LOG.debug(msg);
        mode = Mode.READ;
    }

}
