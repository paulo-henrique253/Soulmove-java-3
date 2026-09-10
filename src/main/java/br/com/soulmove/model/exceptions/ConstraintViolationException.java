package br.com.soulmove.model.exceptions;

public class ConstraintViolationException extends Exception {
    private String constraintName;

    public ConstraintViolationException(String message, String constraintName, Throwable cause) {
        super(message, cause);
        this.constraintName = constraintName;
    }

    public String getConstraintName() {
        return constraintName;
    }
}
