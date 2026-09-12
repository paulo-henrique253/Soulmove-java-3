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

    public void completarMissao(UsuarioSoulMove usuario, Missao missao) throws ConstraintViolationException, NullDataException, DatabaseException, TooLargeException {
        rep.completar(usuario, missao);
    }

    public Missao cadastrar(int pontos, String nome, TipoMissao tipo, String descricao) throws ConstraintViolationException, NullDataException, DatabaseException, TooLargeException {
        return rep.cadastrar(pontos, nome, tipo, descricao);
    }

    public void excluir(Missao missao) throws UnableToFindEntityException, ConstraintViolationException, NullDataException, DatabaseException, TooLargeException {
        rep.excluir(missao);
    }

}
