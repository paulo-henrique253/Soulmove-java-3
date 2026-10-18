package br.com.soulmove.service;

import br.com.soulmove.exceptions.BussinesRuleException;
import br.com.soulmove.exceptions.InvalidDataException;
import br.com.soulmove.exceptions.UnableToFindEntityException;
import br.com.soulmove.model.Missao;
import br.com.soulmove.model.TipoMissao;
import br.com.soulmove.model.UsuarioHistorico;
import br.com.soulmove.model.UsuarioSoulMove;
import br.com.soulmove.repository.MissaoRepository;
import br.com.soulmove.repository.TipoMissaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class MissaoService {
    private final MissaoRepository repository;
    private final TipoMissaoRepository tipoRepository;
    private final UsuarioService usuarioService;

    @Autowired
    public MissaoService(MissaoRepository repository, TipoMissaoRepository tipoRepository, UsuarioService usuarioService) {
        this.repository = repository;
        this.tipoRepository = tipoRepository;
        this.usuarioService = usuarioService;
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
    public Missao cadastrar(int pontos, String nome, String nomeTipo, String descricao, double meta, int periodicidade, int prazo, MultipartFile arquivoImagem){
        TipoMissao tipo = tipoRepository.buscar(nomeTipo);
        if (pontos <=0)
            throw new BussinesRuleException("O atributo pontos deve ser maior que 0.");

        if (nome.isEmpty())
            throw new BussinesRuleException("O titulo não pode ser vazio.");

        if (descricao.isEmpty())
            throw new BussinesRuleException("A descrição não pode ser vazia.");

        byte[] dadosImagem;
        try {
            dadosImagem = arquivoImagem.getBytes();
        } catch (IOException e) {
            throw new InvalidDataException(e.getMessage());
        }

        String tipoMime = arquivoImagem.getContentType();

        long imagemId = repository.cadastrarImagem(dadosImagem, tipoMime);

        long id =  repository.cadastrar(pontos, nome, tipo, descricao, meta, periodicidade, prazo, imagemId);
        Missao m = new Missao(id, nome, tipo, descricao, pontos);
        m.setMeta(meta);
        m.setPeriodicidade(periodicidade);
        m.setPrazo(prazo);
        m.setImagemId(imagemId);
        return m;

    }

    @Transactional
    public void excluir(long missaoId){
        repository.excluirDependencias(missaoId);
        repository.excluir(missaoId);
    }

    public Map<String, Object> buscarImagem(long imagemId) {
        return repository.buscarImagem(imagemId);
    }

    @Transactional
    public void completarMissao(long usuarioId, long missaoId){
        repository.completar(usuarioId, missaoId);
        verificarConcluidas(usuarioId);
    }

    public int verificarConcluidas(long usuarioId){

        List<Missao> pendentes = repository.buscarPendentes(usuarioId);

        UsuarioHistorico historico = usuarioService.buscarHistorico(usuarioId);
        pendentes.removeIf(missao -> !isConcluida(missao, historico));


        for (Missao m : pendentes) {
            completarMissao(usuarioId, m.getId());
        }
        return pendentes.size();
    }

    private boolean isConcluida(Missao missao, UsuarioHistorico historico) {
        TipoMissao tipo = missao.getTipo();

        switch (tipo.getUnidadeMedida()){
            case "kg_C02": return (historico.getKgC02() >= missao.getMeta());
            case "missoes": return (historico.getMissoesConcluidas() >= missao.getMeta());
            case "trajetos": return (historico.getTrajetosLongos() >= missao.getMeta());
        }

        throw new BussinesRuleException("Tipo Missão com unidadeMedida irreconhecivel: " + tipo.getUnidadeMedida());
    }

}
