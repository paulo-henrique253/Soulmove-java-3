package br.com.soulmove.model.type;

public enum StatusMissao {
    CONCLUIDA("concluida"),
    EM_ANDAMENTO("em andamento"),
    CANCELADA("cancelada");

    private String status;

    StatusMissao(String status){
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
