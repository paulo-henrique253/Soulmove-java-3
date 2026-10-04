package br.com.soulmove.service;

import br.com.soulmove.model.UsuarioSoulMove;
import br.com.soulmove.model.Viagem;
import br.com.soulmove.model.type.Veiculo;
import br.com.soulmove.repository.ViagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ViagemService {
    private final ViagemRepository repository;

    @Autowired
    public ViagemService(ViagemRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Viagem viajar(String origem, String destino, Veiculo veiculo, double km_percorrido, double carbono_economizado, double carbono_emitido, UsuarioSoulMove usuario){
        return repository.registrar(origem, destino, veiculo, km_percorrido, carbono_economizado, carbono_emitido, usuario);
    }

    public List<Viagem> obterHistorico(long usuarioId){
        return repository.buscarHistorico(usuarioId);
    }

}
