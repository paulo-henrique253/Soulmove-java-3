package br.com.soulmove.model.exceptions;

public class InvalidDataException extends Exception {
    private String campo;
    public InvalidDataException(String message, String campo){
        super(message);
        this.campo = campo;
    }

    public String getCampo() {
        return campo;
    }
}
