
package br.com.ttsec.web.jsf;

/**
 *
 * @author averri
 */
public enum Mode {

    READ(true, false, false, false, false, "Leitura"),
    INSERT(false, true, false, false, false, "Novo cadastro"),
    UPDATE(false, false, true, false, false, "Edição"),
    DELETE(false, false, false, true, false, "Remoção"),
    CELL_UPDATE(false, false, false, false, true, "Edição de célula");

    private boolean read;

    private boolean insert;

    private boolean update;

    private boolean delete;

    private boolean cellUpdate;

    private String description;

    private Mode(boolean read, boolean insert, boolean update, boolean delete,
                 boolean cellUpdate, String description) {
        this.read = read;
        this.insert = insert;
        this.update = update;
        this.delete = delete;
        this.cellUpdate = cellUpdate;
        this.description = description;
    }

    public boolean isInsert() {
        return insert;
    }

    public boolean isRead() {
        return read;
    }

    public boolean isUpdate() {
        return update;
    }

    public boolean isCellUpdate() {
        return cellUpdate;
    }

    public boolean isDelete() {
        return delete;
    }

    public boolean isDeleteOrRead() {
        return delete || read;
    }

    public boolean isDeleteOrUpdate() {
        return delete || update;
    }

    public boolean isDeleteOrCellUpdate() {
        return delete || cellUpdate;
    }

    public boolean isInsertOrUpdate() {
        return insert || update;
    }

    public boolean isReadOrInsert() {
        return read || insert;
    }

    public boolean isReadOrUpdate() {
        return read || update;
    }

    public String getDescription() {
        return description;
    }

}
