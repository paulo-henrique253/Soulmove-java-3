package br.com.soulmove.model.type;

import br.com.soulmove.exceptions.InvalidDataException;

public enum TipoMissao {
    DIARIA("diaria"),
    SEMANAL("semanal"),
    MENSAL("mensal");



    private final String tipo;

    TipoMissao(String tipo){
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public static TipoMissao getTipoMissao(String s){
        for (TipoMissao tm : values()){
            if (tm.getTipo().equalsIgnoreCase(s)){
                return tm;
            }
        }

        throw new InvalidDataException("tipoMissao inválido: " + s);
    }
}
