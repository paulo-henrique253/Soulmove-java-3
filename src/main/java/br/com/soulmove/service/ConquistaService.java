package br.com.soulmove.service;

import br.com.soulmove.exceptions.BussinesRuleException;
import br.com.soulmove.exceptions.UnableToFindEntityException;
import br.com.soulmove.model.*;
import br.com.soulmove.repository.ConquistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConquistaService {
    private final ConquistaRepository repository;
    private final UsuarioService usuarioService;

    @Autowired
    public ConquistaService(ConquistaRepository repository, UsuarioService usuarioService) {
        this.repository = repository;
        this.usuarioService = usuarioService;
    }

    public List<Conquista> buscarConquistas(){
        return repository.buscarTodas();
    }

    public  List<Conquista> buscarConquistas(long usuarioId){
        List<Conquista> conquistas = repository.buscarTodas(usuarioId);
        UsuarioHistorico historico = usuarioService.buscarHistorico(usuarioId);
        for (Conquista c : conquistas){
            c.setProgresso(obterProgresso(c, historico));
        }
        return conquistas;
    }

    public List<Conquista> buscarConcluidas(long usuarioId){
        return repository.buscarConcluidas(usuarioId);
    }

    public Conquista buscar(long id){
        return repository.buscar(id);
    }


    @Transactional
    public Conquista cadastrar(int pontos, String nome, String titulo, String descricao, double meta, String nomeTipo){
        if (pontos <=0)
            throw new BussinesRuleException("O atributo pontos deve ser maior que 0.");

        if (nome.isEmpty())
            throw new BussinesRuleException("O nome não pode ser vazio.");

        if (titulo.isEmpty())
            throw new BussinesRuleException("O titulo não pode ser vazio.");

        if (descricao.isEmpty())
            throw new BussinesRuleException("A descrição não pode ser vazia.");



        long id = repository.cadastrar(pontos, nome, titulo, descricao, meta, nomeTipo);
        Conquista c = new Conquista();
        c.setId(id);
        c.setPontos(pontos);
        c.setNome(nome);
        c.setTitulo(titulo);
        c.setDescricao(descricao);
        c.setMeta(meta);

        TipoConquista t = new TipoConquista();
        t.setNome(nomeTipo);
        c.setTipo(t);

        return c;
    }

    @Transactional
    public void excluir(long conquistaId){
        repository.excluirDependencias(conquistaId);
        repository.excluir(conquistaId);
    }

    @Transactional
    public void editar(long id, int pontos, String nome, String titulo, String descricao){
        if (pontos <=0)
            throw new BussinesRuleException("O atributo pontos deve ser maior que 0.");

        if (nome.isEmpty())
            throw new BussinesRuleException("O nome não pode ser vazio.");

        if (titulo.isEmpty())
            throw new BussinesRuleException("O titulo não pode ser vazio.");

        if (descricao.isEmpty())
            throw new BussinesRuleException("A descrição não pode ser vazia.");
        int retorno = repository.editar(id, pontos, nome, titulo, descricao);
        if (retorno == 0) throw new UnableToFindEntityException("Conquista não encontrada. Id: " + id);
    }


    @Transactional
    public void completarConquista(long usuarioId, long conquistaId){
        repository.completar(usuarioId, conquistaId);
    }

    public int verificarConcluidas(long usuarioId){

        List<Conquista> pendentes = repository.buscarPendentes(usuarioId);

        UsuarioHistorico historico = usuarioService.buscarHistorico(usuarioId);
        pendentes.removeIf(conquista -> !isConcluida(conquista, historico));


        for (Conquista c : pendentes) {
            completarConquista(usuarioId, c.getId());
        }
        return pendentes.size();
    }

    private boolean isConcluida(Conquista conquista, UsuarioHistorico historico) {
        TipoConquista tipo = conquista.getTipo();

        switch (tipo.getUnidadeMedida()){
            case "Transporte Público": return (historico.getTrajetosLongos() >= conquista.getMeta());
            case "Carbono Economizado": return (historico.getKgC02() >= conquista.getMeta());
            case "Missões": return (historico.getMissoesConcluidas() >= conquista.getMeta());
        }

        throw new BussinesRuleException("Tipo Conquista com unidadeMedida irreconhecivel: " + tipo.getUnidadeMedida());
    }

    private double obterProgresso(Conquista conquista, UsuarioHistorico historico){
        TipoConquista tipo = conquista.getTipo();

        switch (tipo.getUnidadeMedida()){
            case "Transporte Público": return historico.getTrajetosLongos();
            case "Carbono Economizado": return historico.getKgC02();
            case "Missões": return historico.getMissoesConcluidas();
        }
        throw new BussinesRuleException("Tipo Conquista com unidadeMedida irreconhecivel: " + tipo.getUnidadeMedida());
    }

}
