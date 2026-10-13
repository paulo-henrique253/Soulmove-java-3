package br.com.soulmove.exceptions;

public class InvalidDataException extends RuntimeException{

    public InvalidDataException(String message){
        super(message);
    }

}
