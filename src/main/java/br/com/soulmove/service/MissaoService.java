package br.com.soulmove.service;

import br.com.soulmove.exceptions.BussinesRuleException;
import br.com.soulmove.exceptions.UnableToFindEntityException;
import br.com.soulmove.model.Missao;
import br.com.soulmove.model.TipoMissao;
import br.com.soulmove.repository.MissaoRepository;
import br.com.soulmove.repository.TipoMissaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MissaoService {
    private final MissaoRepository repository;
    private final TipoMissaoRepository tipoRepository;

    @Autowired
    public MissaoService(MissaoRepository repository, TipoMissaoRepository tipoRepository) {
        this.repository = repository;
        this.tipoRepository = tipoRepository;
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
    public void editar(long id, int pontos, String titulo, String descricao, String nomeTipo){
        TipoMissao tipo = tipoRepository.buscar(nomeTipo);

        if (pontos <=0)
            throw new BussinesRuleException("O atributo pontos deve ser maior que 0.");

        if (titulo.isEmpty())
            throw new BussinesRuleException("O titulo não pode ser vazio.");

        if (titulo.isEmpty())
            throw new BussinesRuleException("A descrição não pode ser vazia.");

        int retorno = repository.editar(id, pontos, titulo, descricao, tipo);

        if (retorno == 0)
            throw new UnableToFindEntityException("Missao não encontrada. Id: " + id);


    }


    @Transactional
    public Missao cadastrar(int pontos, String nome, String nomeTipo, String descricao, double meta, int periodicidade, int prazo){
        TipoMissao tipo = tipoRepository.buscar(nomeTipo);
        if (pontos <=0)
            throw new BussinesRuleException("O atributo pontos deve ser maior que 0.");

        if (nome.isEmpty())
            throw new BussinesRuleException("O titulo não pode ser vazio.");

        if (descricao.isEmpty())
            throw new BussinesRuleException("A descrição não pode ser vazia.");

        long id =  repository.cadastrar(pontos, nome, tipo, descricao, meta, periodicidade, prazo);
        return new Missao(id, nome, tipo, descricao, pontos);

    }

    @Transactional
    public void excluir(long missaoId){
        repository.excluirDependencias(missaoId);
        repository.excluir(missaoId);
    }

}
