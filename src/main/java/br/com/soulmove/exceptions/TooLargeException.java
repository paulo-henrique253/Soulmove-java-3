package br.com.soulmove.exceptions;

public class TooLargeException extends RuntimeException{

    public TooLargeException(String message, String columnName) {
        super(message);
    }

}
