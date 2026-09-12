package br.com.soulmove.Service;

import br.com.soulmove.model.Conquista;
import br.com.soulmove.model.UsuarioSoulMove;
import br.com.soulmove.model.exceptions.*;
import br.com.soulmove.repository.UsuarioRepository;

import java.sql.SQLException;

public class UsuarioService {
    UsuarioRepository rep = new UsuarioRepository();
    public UsuarioSoulMove cadastrar(String nome, String email, String senha)
            throws ConstraintViolationException, NullDataException, DatabaseException, TooLargeException, InvalidDataException {

        if (!email.contains("@"))
            throw new InvalidDataException("O campo Email precisa de um @", "email");
        return rep.cadastrar(nome, email, senha);

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



}
