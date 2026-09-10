package br.com.soulmove.model.exceptions;

public class TooLargeException extends Exception {
    private String columnName;

    public TooLargeException(String message, String columnName, Throwable e) {

        super(message, e);
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }
}
