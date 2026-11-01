package br.com.soulmove.service;

import br.com.soulmove.exceptions.UnableToFindEntityException;
import br.com.soulmove.model.TipoMissao;
import br.com.soulmove.repository.TipoMissaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoMissaoService {

    private final TipoMissaoRepository repository;

    @Autowired
    public TipoMissaoService(TipoMissaoRepository repository) {
        this.repository = repository;
    }

    public TipoMissao buscar(String nome){
        try{
            return repository.buscar(nome);
        } catch (EmptyResultDataAccessException e){
            throw new UnableToFindEntityException("Tipo missão com esse nome não encontrada");
        }
    }
    public List<TipoMissao> buscarTodos(){
        return repository.buscarTodos();
    }

    public boolean isTipoMissao(String nome){
        return repository.isMissao(nome);
    }

}
