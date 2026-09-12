package br.com.soulmove.model.exceptions;

public class UnableToFindEntityException extends Exception {
    private String tableName;
    public UnableToFindEntityException(String message, String table) {
        super(message);
        this.tableName = table;
    }

    public String getTableName() {
        return tableName;
    }
}
