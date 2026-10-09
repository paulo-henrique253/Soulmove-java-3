package br.com.soulmove.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UsuarioSoulMove extends Usuario {
    private int pontos;
    private Conquista tituloAtual;
    private CarteiraUsuario carteira;
    private List<Missao> missoesConcluidas = new ArrayList<>();
    private List<Conquista> conquistasConcluidas = new ArrayList<>();

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

    public List<Missao> getMissoesConcluidas() {
        return missoesConcluidas;
    }

    public void setMissoesConcluidas(List<Missao> missoesConcluidas) {
        this.missoesConcluidas = missoesConcluidas;
    }

    public List<Conquista> getConquistasConcluidas() {
        return conquistasConcluidas;
    }

    public CarteiraUsuario getCarteira(){ return  this.carteira;}

    public void setCarteira(CarteiraUsuario carteira) {
        this.carteira = carteira;
    }

    public void setConquistasConcluidas(List<Conquista> conquistasConcluidas) {
        this.conquistasConcluidas = conquistasConcluidas;
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

    public void adicionarMissaoConcluida(Missao missao) {
        missoesConcluidas.add(missao);
    }

    public void adicionarConquistaConcluida(Conquista conquista) {
        conquistasConcluidas.add(conquista);
    }
}
