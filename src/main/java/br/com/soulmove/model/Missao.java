package br.com.soulmove.model;

import br.com.soulmove.model.type.TipoMissao;
import br.com.soulmove.model.type.TipoVeiculo;

public class Missao {
    private long id;
    private String titulo;
    private TipoMissao tipo;
    private String descricao;
    private int pontos;


    public Missao() {
    }

    public Missao(long id, String titulo, TipoMissao tipo, String descricao, int pontos) {
        this.id = id;
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
}
