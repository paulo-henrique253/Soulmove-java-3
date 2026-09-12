package br.com.soulmove.model;

import java.time.LocalDate;

public class UsuarioSoulMove extends Usuario {
    public int pontos;
    private Conquista tituloAtual;
    private UsuarioHistorico historico;

    public UsuarioSoulMove() {
        super();
    }

    public UsuarioSoulMove(long id, String nome, int pontos, String email, LocalDate dataCadastro, String senha) {
        super(id, nome, email, dataCadastro, senha);
        this.pontos = pontos;
    }

    public int getPontos() {
        return pontos;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    public Conquista getTituloAtual() {
        return tituloAtual;
    }

    public void setTituloAtual(Conquista tituloAtual) {
        this.tituloAtual = tituloAtual;
    }

    @Override
    public String toString() {
        String t = "Nenhum";
        if(tituloAtual != null){
            t = tituloAtual.getTitulo();
        }
        return super.toString() +
                "\npontos: " + pontos +
                "\ntituloAtual: " + t
                ;
    }
}
