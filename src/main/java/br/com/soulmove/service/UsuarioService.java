package br.com.soulmove.service;

import br.com.soulmove.exceptions.BussinesRuleException;
import br.com.soulmove.exceptions.InvalidDataException;
import br.com.soulmove.model.Conquista;
import br.com.soulmove.model.Missao;
import br.com.soulmove.model.UsuarioSoulMove;
import br.com.soulmove.repository.ConquistaRepository;
import br.com.soulmove.repository.MissaoRepository;
import br.com.soulmove.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {
    private final double taxaDeConversao = 0.009;
    private final UsuarioRepository repository;
    private final MissaoRepository missaoRepository;
    private final ConquistaRepository conquistaRepository;
    private final CarteiraService carteiraService;

    @Autowired
    public UsuarioService (UsuarioRepository repository, MissaoRepository missaoRepository, ConquistaRepository conquistaRepository, CarteiraService carteiraService){
        this.repository = repository;
        this.missaoRepository = missaoRepository;
        this.conquistaRepository = conquistaRepository;
        this.carteiraService = carteiraService;
    }

    @Transactional
    public UsuarioSoulMove cadastrar(String nome, String email, String senha){

        if (!email.contains("@"))
            throw new InvalidDataException("Email invalido");
        if (nome.isEmpty())
            throw new BussinesRuleException("O campo nome não pode ser vazio");
        if (senha.length() < 6)
            throw new BussinesRuleException("A senha precisa ter mais de 6 caracteres");

        UsuarioSoulMove usuario = repository.cadastrar(nome, email, senha);

        usuario.setCarteira(carteiraService.cadastrar(usuario));

        return usuario;

    }

    public UsuarioSoulMove logar(String email, String senha){

        UsuarioSoulMove usuario = repository.buscar(email);

        if(usuario.isSenha(senha)){
            usuario.setConquistasConcluidas(conquistaRepository.buscarConcluidas(usuario.getId()));
            usuario.setMissoesConcluidas(missaoRepository.buscarConcluidas(usuario.getId()));
            return usuario;
        }

        return null;
    }

    @Transactional
    public void alterarTitulo(UsuarioSoulMove usuario, Conquista conquista){

        repository.alterarConquista(usuario, conquista);

        usuario.setTituloAtual(conquista);

    }

    @Transactional
    public void completarMissao(UsuarioSoulMove usuario, Missao missao){
        missaoRepository.completar(usuario, missao);

        aumentarPontos(usuario, missao.getPontos());
        usuario.adicionarMissaoConcluida(missao);


    }

    @Transactional
    public void completarConquista(UsuarioSoulMove usuario, Conquista conquista){
        conquistaRepository.completar(usuario, conquista);

        aumentarPontos(usuario, conquista.getPontos());
        usuario.adicionarConquistaConcluida(conquista);
    }

    @Transactional
    public void aumentarPontos(UsuarioSoulMove usuario, int pontos){
        if (pontos <= 0)
            throw new BussinesRuleException("A quantidade de pontos deve ser maior que 0.");
        repository.alterarPontos(usuario, usuario.getPontos() + pontos);
        usuario.setPontos(usuario.getPontos() + pontos);
    }

    @Transactional
    public void resgatarPontos(UsuarioSoulMove usuario, int quantidade){
        if (quantidade * taxaDeConversao < 10.0)
            throw new BussinesRuleException("A Quandidade minima de pontos a ser sacada é de R$10,00 em pontos (" +Math.ceil(10.0 /taxaDeConversao)+ " pontos)");
        if(usuario.getPontos() < quantidade){
            throw new BussinesRuleException("Quantidade inserida maior que a quantidade de pontos disponivel");
        }


        int pontos = usuario.getPontos() - quantidade;

        repository.alterarPontos(usuario, pontos);
        usuario.setPontos(pontos);

        carteiraService.alterarSaldo(usuario.getCarteira(), quantidade * taxaDeConversao);




    }


    public void atualizar(UsuarioSoulMove usuario){
        usuario = repository.buscar(usuario.getId());
    }


}
