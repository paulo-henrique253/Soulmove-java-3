package br.com.soulmove.model;

public class CarteiraUsuario {
    private long id;
    private double saldo;


    public CarteiraUsuario(long id, double saldo) {
        this.id = id;
        this.saldo = saldo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
}
