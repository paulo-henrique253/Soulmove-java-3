package br.com.soulmove.Service;

import br.com.soulmove.model.UsuarioSoulMove;
import br.com.soulmove.model.Viagem;
import br.com.soulmove.model.exceptions.*;
import br.com.soulmove.model.type.Veiculo;
import br.com.soulmove.repository.ViagemRepository;

import java.util.List;

public class ViagemService {
    ViagemRepository rep = new ViagemRepository();

    public Viagem viajar(String origem, String destino, Veiculo veiculo, double km_percorrido, double carbono_economizado, double carbono_emitido, UsuarioSoulMove usuario) throws ConstraintViolationException, NullDataException, DatabaseException, TooLargeException {
        return rep.registrar(origem, destino, veiculo, km_percorrido, carbono_economizado, carbono_emitido, usuario);
    }

    public List<Viagem> obterHistorico(UsuarioSoulMove usuario) throws UnableToFindEntityException, ConstraintViolationException, NullDataException, DatabaseException, TooLargeException {
        return rep.buscarHistorico(usuario);
    }

}
