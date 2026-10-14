package br.com.soulmove.service;

import br.com.soulmove.api.CalculadorDeRotas;
import br.com.soulmove.exceptions.InvalidDataException;
import br.com.soulmove.model.UsuarioSoulMove;
import br.com.soulmove.model.Viagem;
import br.com.soulmove.model.type.Veiculo;
import br.com.soulmove.repository.UsuarioRepository;
import br.com.soulmove.repository.ViagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ViagemService {
    private final ViagemRepository repository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public ViagemService(ViagemRepository repository, UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Viagem viajar(String origem, String destino, Veiculo veiculo, long usuarioId){
        Viagem v = simularViagem(origem, destino, veiculo);
        UsuarioSoulMove usuario = usuarioRepository.buscar(usuarioId);
        return repository.registrar(origem, destino, veiculo, v.getKmPercorrido(), v.getCarbonoEconomizado(), v.getCarbonoEmitido(), usuario);
    }

    public List<Viagem> obterHistorico(long usuarioId){
        return repository.buscarHistorico(usuarioId);
    }

    public Viagem simularViagem(String origem, String destino, Veiculo veiculo) {

        CalculadorDeRotas.Coordenadas coordenadasO;
        try {
            coordenadasO = CalculadorDeRotas.buscarCoordenadas(origem);
        } catch (Exception e) {
            throw new InvalidDataException("Coordenadas da origem não encontradas. Origem: " + origem + ". Tente o formato: Rua -------, numero, nome da cidade.");
        }

        CalculadorDeRotas.Coordenadas coordenadasD;
        try {
            coordenadasD = CalculadorDeRotas.buscarCoordenadas(destino);
        } catch (Exception e) {
            throw new InvalidDataException("Coordenadas do destino não encontradas. Destino: " + destino + ". Tente o formato: Rua -------, numero, nome da cidade.");
        }


        double kmPercorridos;
        try {
            kmPercorridos = CalculadorDeRotas.calcularRota(coordenadasO, coordenadasD);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        double carbonoEmitido = kmPercorridos * veiculo.getEmissao();
        double carbonoEconomizado = (Veiculo.CARRO.getEmissao() * kmPercorridos) - carbonoEmitido;

        Viagem v = new Viagem();
        v.setOrigem(origem);
        v.setDestino(destino);
        v.setTipoVeiculo(veiculo);
        v.setKmPercorrido(kmPercorridos);
        v.setCarbonoEmitido(carbonoEmitido);
        v.setCarbonoEconomizado(carbonoEconomizado);

        return v;

    }

}
