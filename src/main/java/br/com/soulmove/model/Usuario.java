package br.com.soulmove.model;

import java.time.LocalDate;

public class Usuario {
    private long id;
    private String nome;
    private String email;
    private LocalDate dataCadastro;
    private String senha;

    public Usuario (){}

    public Usuario(long id, String nome, String email, LocalDate dataCadastro, String senha) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.dataCadastro = dataCadastro;
        this.senha = senha;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public boolean setSenha(String senhaAtual, String novaSenha){
        if (this.senha.equals(senhaAtual)){
            this.senha = novaSenha;
            return true;
        }
        return false;
    }

    public boolean isSenha(String senha){
        return this.senha.equals(senha);
    }
}
