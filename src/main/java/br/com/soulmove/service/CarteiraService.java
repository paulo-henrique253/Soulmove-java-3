package br.com.soulmove.service;

import br.com.soulmove.exceptions.DuplicateEntityException;
import br.com.soulmove.exceptions.InvalidDataException;
import br.com.soulmove.exceptions.UnableToFindEntityException;
import br.com.soulmove.model.CarteiraUsuario;
import br.com.soulmove.model.UsuarioSoulMove;
import br.com.soulmove.repository.CarteiraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
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
        try{
        return repository.cadastrar(usuario);
        }catch (DuplicateKeyException e){
            throw new DuplicateEntityException("Usuário já possui carteira cadastrada.");
        }
    }

    @Transactional
    public void alterarSaldo(CarteiraUsuario carteira, double quantidade){
        double saldo = carteira.getSaldo() + quantidade;
        try{
            int registros = repository.alterarSaldo(carteira, saldo);
            if (registros == 0)
                throw new UnableToFindEntityException("Carteira não encontrada");
        }catch (DataIntegrityViolationException e){
            throw new InvalidDataException("Quandidade de pontos inválida");
        }
        carteira.setSaldo(saldo);
    }

    public CarteiraUsuario buscar(long usuarioId){
        try{
            return repository.buscar(usuarioId);
        } catch (EmptyResultDataAccessException e){
            throw new UnableToFindEntityException("Carteira com esse usuário não encontrada");
        }
    }

    public CarteiraUsuario buscarCarteira(long id) {
        try{
            return repository.buscarCarteira(id);
        }catch (EmptyResultDataAccessException e){
            throw new UnableToFindEntityException("Carteira não encontrada");
        }
    }
}
