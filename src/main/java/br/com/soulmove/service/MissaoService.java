package br.com.soulmove.service;

import br.com.soulmove.model.Missao;
import br.com.soulmove.model.type.TipoMissao;
import br.com.soulmove.repository.MissaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MissaoService {
    private final MissaoRepository repository;

    @Autowired
    public MissaoService(MissaoRepository repository) {
        this.repository = repository;
    }

    public List<Missao> buscarMissoes(){
        return repository.buscarTodas();
    }

    public List<Missao> buscarConcluidas(long usuarioId){
        return repository.buscarConcluidas(usuarioId);
    }

    public Missao buscar(long id){
        return repository.buscar(id);
    }

    @Transactional
    public void editar(long id, int pontos, String titulo, String descricao, TipoMissao tipo){
        repository.editar(id, pontos, titulo, descricao, tipo);
    }


    @Transactional
    public Missao cadastrar(int pontos, String nome, TipoMissao tipo, String descricao){
        long id = repository.cadastrar(pontos, nome, tipo, descricao);
        return new Missao(id, nome, tipo, descricao, pontos);
    }

    @Transactional
    public void excluir(long missaoId){
        repository.excluirDependencias(missaoId);
        repository.excluir(missaoId);
    }

}
