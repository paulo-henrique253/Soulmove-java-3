package br.com.soulmove.service;

import br.com.soulmove.exceptions.BussinesRuleException;
import br.com.soulmove.exceptions.InvalidDataException;
import br.com.soulmove.model.UsuarioSoulMove;
import br.com.soulmove.repository.RecargaRepository;
import br.com.soulmove.repository.UsuarioRepository;
import br.com.soulmove.utils.PixValidator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class RecargaService {
    private final PixValidator pixValidator;
    private final UsuarioService usuarioService;
    private final RecargaRepository repository;

    public RecargaService(PixValidator pixValidator, UsuarioService usuarioService, RecargaRepository repository) {
        this.pixValidator = pixValidator;
        this.usuarioService = usuarioService;
        this.repository = repository;
    }

    public void processarRecargaPix(String pix, long usuarioId){

        // verifica se o texto foi adulterado
        if (!pixValidator.validarCrc(pix))
            throw new InvalidDataException("Código pix corrompido ou adulterado: Crc inválido");

        // verifica se o pix foi gerado pela SPTrans
        if (!pixValidator.validarPixSpTrans(pix))
            throw new BussinesRuleException("Código não pertencente á SPTrans");

        BigDecimal valorRecarga = pixValidator.extrairValor(pix);

        if (valorRecarga == null || valorRecarga.compareTo(BigDecimal.ZERO) <= 0)
            throw new InvalidDataException("O Pix não possui um valor definido");

        usuarioService.efetuarPagamento(usuarioId, valorRecarga);
        repository.cadastrar(usuarioId, pix, valorRecarga);

    }

}
