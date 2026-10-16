package br.com.soulmove.model;

import br.com.soulmove.model.TipoMissao;

import java.time.LocalDate;

public class Missao {
    private long id;
    private String titulo;
    private TipoMissao tipo;
    private String descricao;
    private int pontos;
    private LocalDate dataConclusao; // guarda a data de conclusão para missoes obtidas a partir de um usuario que as completou
    private double meta;
    private int periodicidade; //em dias
    private int prazo; //em dias

    public Missao() {
    }

    public Missao(long id, String titulo, TipoMissao tipo, String descricao, int pontos) {
        this.id = id;
        this.titulo = titulo;
        this.tipo = tipo;
        this.descricao = descricao;
        this.pontos = pontos;
    }

    public Missao(String titulo, TipoMissao tipo, String descricao, int pontos) {
        this.titulo = titulo;
        this.tipo = tipo;
        this.descricao = descricao;
        this.pontos = pontos;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public TipoMissao getTipo() {
        return tipo;
    }

    public void setTipo(TipoMissao tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getPontos() {
        return pontos;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    public LocalDate getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(LocalDate dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    public double getMeta() {
        return meta;
    }

    public void setMeta(double meta) {
        this.meta = meta;
    }

    public int getPeriodicidade() {
        return periodicidade;
    }

    public void setPeriodicidade(int periodicidade) {
        this.periodicidade = periodicidade;
    }

    public int getPrazo() {
        return prazo;
    }

    public void setPrazo(int prazo) {
        this.prazo = prazo;
    }

    @Override
    public String toString() {
        return  "Id: " + id +
                "\nTitulo: '" + titulo + '\'' +
                "\nTipo: " + tipo.getNome() +
                "\nDescricao: '" + descricao + '\'' +
                "\nPontos: " + pontos
                ;
    }
}
