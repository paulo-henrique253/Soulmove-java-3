package br.com.soulmove.exceptions;

public class UnableToFindEntityException extends RuntimeException {
    public UnableToFindEntityException(String message) {
        super(message);
    }

}
