package br.com.soulmove.service;

import br.com.soulmove.model.Conquista;
import br.com.soulmove.repository.ConquistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConquistaService {
    private final ConquistaRepository repository;

    @Autowired
    public ConquistaService(ConquistaRepository repository) {
        this.repository = repository;
    }

    public List<Conquista> buscarConquistas(){
        return repository.buscarTodas();
    }

    public List<Conquista> buscarConcluidas(long usuarioId){
        return repository.buscarConcluidas(usuarioId);
    }

    public Conquista buscar(long id){
        return repository.buscar(id);
    }


    @Transactional
    public Conquista cadastrar(int pontos, String nome, String titulo, String descricao){
        long id = repository.cadastrar(pontos, nome, titulo, descricao);
        Conquista c = new Conquista();
        c.setId(id);
        c.setPontos(pontos);
        c.setNome(nome);
        c.setTitulo(titulo);
        c.setDescricao(descricao);

        return c;
    }

    @Transactional
    public void excluir(long conquistaId){
        repository.excluirDependencias(conquistaId);
        repository.excluir(conquistaId);
    }

    @Transactional
    public void editar(long id, int pontos, String nome, String titulo, String descricao){
        repository.editar(id, pontos, nome, titulo, descricao);
    }
}
