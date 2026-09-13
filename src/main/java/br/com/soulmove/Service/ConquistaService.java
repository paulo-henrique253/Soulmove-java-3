package br.com.soulmove.Service;

import br.com.soulmove.model.Conquista;
import br.com.soulmove.model.UsuarioSoulMove;
import br.com.soulmove.model.exceptions.*;
import br.com.soulmove.repository.ConquistaRepository;

import java.util.List;

public class ConquistaService {
    private ConquistaRepository rep = new ConquistaRepository();
    public List<Conquista> buscarConquistas() throws ConstraintViolationException, NullDataException, DatabaseException, TooLargeException {
        return rep.buscarTodas();
    }

    public List<Conquista> buscarConcluidas(UsuarioSoulMove usuario) throws ConstraintViolationException, NullDataException, DatabaseException, TooLargeException {
        return rep.buscarConcluidas(usuario);
    }
    public Conquista buscar(long id) throws UnableToFindEntityException, ConstraintViolationException, NullDataException, DatabaseException, TooLargeException {
        return rep.buscar(id);
    }

    public void completarConquista(UsuarioSoulMove usuario, Conquista conquista) throws ConstraintViolationException, NullDataException, DatabaseException, TooLargeException, UnableToFindEntityException {
        rep.completar(usuario, conquista);

        UsuarioService usuarioService = new UsuarioService();
        usuarioService.aumentarPontos(usuario, conquista.getPontos());
    }

    public Conquista cadastrar(int pontos, String nome, String titulo, String descricao) throws ConstraintViolationException, NullDataException, DatabaseException, TooLargeException {
        return rep.cadastrar(pontos, nome, titulo, descricao);
    }

    public void excluir(Conquista conquista) throws UnableToFindEntityException, ConstraintViolationException, NullDataException, DatabaseException, TooLargeException {
        rep.excluir(conquista);
    }

    public void editar(long id, Conquista conquista) throws UnableToFindEntityException, ConstraintViolationException, NullDataException, DatabaseException, TooLargeException {

        rep.editar(id, conquista);
    }
}
