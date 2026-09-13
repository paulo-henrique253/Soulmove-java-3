package br.com.soulmove.Service;

import br.com.soulmove.model.Missao;
import br.com.soulmove.model.UsuarioSoulMove;
import br.com.soulmove.model.exceptions.*;
import br.com.soulmove.model.type.TipoMissao;
import br.com.soulmove.repository.MissaoRepository;

import java.util.List;

public class MissaoService {
    MissaoRepository rep = new MissaoRepository();

    public List<Missao> buscarMissoes() throws ConstraintViolationException, NullDataException, DatabaseException, TooLargeException {
        return rep.buscarTodas();
    }

    public List<Missao> buscarConcluidas(UsuarioSoulMove usuario) throws ConstraintViolationException, NullDataException, DatabaseException, TooLargeException {
        return rep.buscarConcluidas(usuario);
    }

    public Missao buscar(long id) throws UnableToFindEntityException, ConstraintViolationException, NullDataException, DatabaseException, TooLargeException {
        return rep.buscar(id);
    }
    public void editar(long id, Missao missao) throws UnableToFindEntityException, ConstraintViolationException, NullDataException, DatabaseException, TooLargeException {
        rep.editar(id, missao);
    }

    public void completarMissao(UsuarioSoulMove usuario, Missao missao) throws ConstraintViolationException, NullDataException, DatabaseException, TooLargeException, UnableToFindEntityException {
        rep.completar(usuario, missao);

        UsuarioService usuarioService = new UsuarioService();
        usuarioService.aumentarPontos(usuario, missao.getPontos());
    }

    public Missao cadastrar(int pontos, String nome, TipoMissao tipo, String descricao) throws ConstraintViolationException, NullDataException, DatabaseException, TooLargeException {
        return rep.cadastrar(pontos, nome, tipo, descricao);
    }

    public void excluir(Missao missao) throws UnableToFindEntityException, ConstraintViolationException, NullDataException, DatabaseException, TooLargeException {
        rep.excluirDependencias(missao);
        rep.excluir(missao);
    }

}
