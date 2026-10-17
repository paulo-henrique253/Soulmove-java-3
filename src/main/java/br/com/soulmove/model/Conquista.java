package br.com.soulmove.model;

import java.time.LocalDate;

public class Conquista {
    private long id;
    private String nome;
    private String descricao;
    private String titulo;
    private int pontos;
    private double meta;
    private TipoConquista tipo;
    private LocalDate dataConclusao; // guarda a data de conclusão para conquistas obtidas a partir de um usuario que as completou

    public Conquista() {
    }

    public Conquista(long id, String nome, String descricao, int pontos, String titulo) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.pontos = pontos;
        this.titulo = titulo;
    }

    public Conquista(String nome, String titulo, int pontos, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
        this.pontos = pontos;
        this.titulo = titulo;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public TipoConquista getTipo() {
        return tipo;
    }

    public void setTipo(TipoConquista tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return  "Id: " + id +
                "\nNome: '" + nome + '\'' +
                "\nDescricao: '" + descricao + '\'' +
                "\nTitulo: '" + titulo + '\'' +
                "\nPontos: " + pontos
                ;
    }
}
