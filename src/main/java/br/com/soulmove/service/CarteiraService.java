package br.com.soulmove.service;

import br.com.soulmove.model.CarteiraUsuario;
import br.com.soulmove.model.UsuarioSoulMove;
import br.com.soulmove.repository.CarteiraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CarteiraService {
    private CarteiraRepository repository;
    @Autowired
    public CarteiraService(CarteiraRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public CarteiraUsuario cadastrar(UsuarioSoulMove usuario){
        return repository.cadastrar(usuario);
    }

    @Transactional
    public void alterarSaldo(CarteiraUsuario carteira, double quantidade){
        double saldo = carteira.getSaldo() + quantidade;
        repository.alterarSaldo(carteira, saldo);
        carteira.setSaldo(saldo);
    }

    public CarteiraUsuario buscar(long usuarioId){
        return repository.buscar(usuarioId);
    }
}
