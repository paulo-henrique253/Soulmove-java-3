package br.com.soulmove.service;

import br.com.soulmove.exceptions.BussinesRuleException;
import br.com.soulmove.exceptions.DuplicateEntityException;
import br.com.soulmove.exceptions.InvalidDataException;
import br.com.soulmove.exceptions.UnableToFindEntityException;
import br.com.soulmove.model.Conquista;
import br.com.soulmove.model.UsuarioHistorico;
import br.com.soulmove.model.UsuarioSoulMove;
import br.com.soulmove.repository.ConquistaRepository;
import br.com.soulmove.repository.MissaoRepository;
import br.com.soulmove.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

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
        try {
            UsuarioSoulMove usuario = repository.cadastrar(nome, email, senha);
            usuario.setCarteira(carteiraService.cadastrar(usuario));

            return usuario;
        } catch (DataIntegrityViolationException e){
            Throwable rootCause = e.getMostSpecificCause();
            String message = rootCause.getMessage().toUpperCase();
            if (message != null){
                if (message.contains("EMAIL"))
                    throw new DuplicateEntityException("Email ja cadastrado");
            }
            throw e;
        }
    }

    public UsuarioSoulMove logar(String email, String senha){

        try{
        UsuarioSoulMove usuario = repository.buscar(email);

        if(usuario.isSenha(senha)){
            usuario.setConquistasConcluidas(conquistaRepository.buscarConcluidas(usuario.getId()));
            usuario.setMissoesConcluidas(missaoRepository.buscarConcluidas(usuario.getId()));
            return usuario;
        }

        return null;
        } catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    @Transactional
    public void alterarTitulo(UsuarioSoulMove usuario, Conquista conquista){
        repository.alterarConquista(usuario.getId(), conquista.getId());
        usuario.setTituloAtual(conquista);
    }

    @Transactional
    public UsuarioSoulMove alterarTitulo(long usuarioId, long conquistaId){
        List<Conquista> concluidas = conquistaRepository.buscarConcluidas(usuarioId);
        if (concluidas.isEmpty())
            throw new UnableToFindEntityException("Impossivel obter conquistas concluidas");

        for (Conquista c : concluidas){
            if (c.getId() == conquistaId)
            {
                    repository.alterarConquista(usuarioId, conquistaId);

                    return repository.buscar(usuarioId);

            }
        }

        throw new BussinesRuleException("ConquistaId deve ser uma conquista concluida");
    }


    @Transactional
    public void efetuarPagamento(long usuarioId, BigDecimal valor){
        try{
            UsuarioSoulMove usuario = repository.buscar(usuarioId);

            double valorEmPontos = Math.floor(valor.doubleValue() / taxaDeConversao);

            if (valorEmPontos > usuario.getPontos())
                throw new BussinesRuleException("Valor a ser pago maior que o equivalente em pontos dispobivel");

            carteiraService.alterarSaldo(usuario.getCarteira(), valor.doubleValue());
        }catch (EmptyResultDataAccessException e){
            throw new UnableToFindEntityException("Usuário não encontrado");
        }
    }

    public UsuarioHistorico buscarHistorico(long id) {
        try {
            return repository.buscarHistorico(id);
        }catch (EmptyResultDataAccessException e){
            throw new UnableToFindEntityException("Usuário não encontrado");
        }

    }
}
