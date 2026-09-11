package br.com.soulmove.model;

import br.com.soulmove.model.type.Veiculo;

import java.time.LocalDate;

public class Viagem {
    private long id;
    private LocalDate data;
    private String origem;
    private String destino;
    private Veiculo tipoVeiculo;
    private double kmPercorrido;
    private double carbonoEconomizado;
    private double carbonoEmitido;
    private UsuarioSoulMove usuario;

    public Viagem(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Veiculo getTipoVeiculo() {
        return tipoVeiculo;
    }

    public void setTipoVeiculo(Veiculo tipoVeiculo) {
        this.tipoVeiculo = tipoVeiculo;
    }

    public double getKmPercorrido() {
        return kmPercorrido;
    }

    public void setKmPercorrido(double kmPercorrido) {
        this.kmPercorrido = kmPercorrido;
    }

    public double getCarbonoEconomizado() {
        return carbonoEconomizado;
    }

    public void setCarbonoEconomizado(double carbonoEconomizado) {
        this.carbonoEconomizado = carbonoEconomizado;
    }

    public double getCarbonoEmitido() {
        return carbonoEmitido;
    }

    public void setCarbonoEmitido(double carbonoEmitido) {
        this.carbonoEmitido = carbonoEmitido;
    }

    public UsuarioSoulMove getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioSoulMove usuario) {
        this.usuario = usuario;
    }
}
