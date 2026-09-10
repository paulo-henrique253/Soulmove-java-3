package br.com.soulmove.model;

public class Conquista {
    private long id;
    private String nome;
    private String descricao;
    private String titulo;
    private int pontos;

    public Conquista() {
    }

    public Conquista(long id, String nome, String descricao, int pontos, String titulo) {
        this.id = id;
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

    @Override
    public String toString() {
        return this.nome + "\t| " + this.pontos + "\t| " + this.titulo + "\t| " + this.descricao;
    }
}
