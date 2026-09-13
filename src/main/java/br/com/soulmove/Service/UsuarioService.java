package br.com.soulmove.Service;

import br.com.soulmove.model.CarteiraUsuario;
import br.com.soulmove.model.Conquista;
import br.com.soulmove.model.UsuarioSoulMove;
import br.com.soulmove.model.exceptions.*;
import br.com.soulmove.repository.UsuarioRepository;

import java.sql.SQLException;

public class UsuarioService {
    private final double taxaDeConversao = 0.009;
    UsuarioRepository rep = new UsuarioRepository();
    public UsuarioSoulMove cadastrar(String nome, String email, String senha)
            throws ConstraintViolationException, NullDataException, DatabaseException, TooLargeException, InvalidDataException {

        if (!email.contains("@"))
            throw new InvalidDataException("O campo Email precisa de um @", "email");

        UsuarioSoulMove usuario = rep.cadastrar(nome, email, senha);
        CarteiraService carteiraService = new CarteiraService();
        carteiraService.cadastrar(usuario);
        return usuario;

    }

    public UsuarioSoulMove logar(String email, String senha) throws UnableToFindEntityException, ConstraintViolationException, NullDataException, DatabaseException, TooLargeException {

        UsuarioSoulMove usuario = rep.buscar(email);

        if(usuario.isSenha(senha))
            return usuario;
        return null;
    }

    public void alterarTitulo(UsuarioSoulMove usuario, Conquista conquista) throws UnableToFindEntityException, ConstraintViolationException, NullDataException, DatabaseException, TooLargeException {

        rep.alterarConquista(usuario, conquista);

        usuario.setTituloAtual(conquista);

    }

    public void aumentarPontos(UsuarioSoulMove usuario, int pontos) throws UnableToFindEntityException, ConstraintViolationException, NullDataException, DatabaseException, TooLargeException {
        rep.alterarPontos(usuario, usuario.getPontos() + pontos);
        usuario.setPontos(usuario.getPontos() + pontos);
    }

    public void resgatarPontos(UsuarioSoulMove usuario, int quantidade) throws InvalidDataException, UnableToFindEntityException, ConstraintViolationException, NullDataException, DatabaseException, TooLargeException {
        if(usuario.getPontos() < quantidade){
            throw new InvalidDataException("Quantidade maior que o disponivel", "pontos");
        }
        CarteiraService carteiraService = new CarteiraService();

        int pontos = usuario.getPontos() - quantidade;

        rep.alterarPontos(usuario, pontos);
        usuario.setPontos(pontos);

        CarteiraUsuario carteira = carteiraService.buscar(usuario);
        carteiraService.alterarSaldo(carteira, quantidade * taxaDeConversao);




    }


    public void atualizar(UsuarioSoulMove usuario) throws UnableToFindEntityException, ConstraintViolationException, NullDataException, DatabaseException, TooLargeException {
        usuario = rep.buscar(usuario.getId());
    }


}
