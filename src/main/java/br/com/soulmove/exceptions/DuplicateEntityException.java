package br.com.soulmove.exceptions;

public class DuplicateEntityException extends RuntimeException {
    public DuplicateEntityException(String s) {
        super(s);
    }
}
