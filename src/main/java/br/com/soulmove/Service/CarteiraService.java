package br.com.soulmove.Service;

import br.com.soulmove.model.CarteiraUsuario;
import br.com.soulmove.model.UsuarioSoulMove;
import br.com.soulmove.model.exceptions.*;
import br.com.soulmove.repository.CarteiraRepository;

public class CarteiraService {
    private CarteiraRepository rep = new CarteiraRepository();

    public CarteiraUsuario cadastrar(UsuarioSoulMove usuario) throws ConstraintViolationException, NullDataException, DatabaseException, TooLargeException {
        return rep.cadastrar(usuario);
    }

    public void alterarSaldo(CarteiraUsuario carteira, double quantidade) throws UnableToFindEntityException, ConstraintViolationException, NullDataException, DatabaseException, TooLargeException {
        double saldo = carteira.getSaldo() + quantidade;
        rep.alterarSaldo(carteira, saldo);
        carteira.setSaldo(saldo);
    }

    public CarteiraUsuario buscar(UsuarioSoulMove usuario) throws UnableToFindEntityException, ConstraintViolationException, NullDataException, DatabaseException, TooLargeException {
        return rep.buscar(usuario);
    }
}
