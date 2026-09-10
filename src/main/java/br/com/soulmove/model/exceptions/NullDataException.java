package br.com.soulmove.model.exceptions;

public class NullDataException extends Exception {
    private String columnName;
    public NullDataException(String message, String columnName, Throwable e) {
        super(message, e);
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }
}
