package br.com.soulmove.model;

public class CarteiraUsuario {
    private long id;
    private UsuarioSoulMove usuario;
    private double saldo;


    public CarteiraUsuario(long id, UsuarioSoulMove usuario, double saldo) {
        this.id = id;
        this.usuario = usuario;
        this.saldo = saldo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UsuarioSoulMove getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioSoulMove usuario) {
        this.usuario = usuario;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
}
