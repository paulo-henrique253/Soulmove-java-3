package br.com.soulmove.model.exceptions;

public class UnableToFindEntityException extends RuntimeException {
    public UnableToFindEntityException(String message) {
        super(message);
    }
}
