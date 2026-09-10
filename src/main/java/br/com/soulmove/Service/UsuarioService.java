package br.com.soulmove.Service;

import br.com.soulmove.model.UsuarioSoulMove;
import br.com.soulmove.repository.UsuarioRepository;

import java.sql.SQLException;

public class UsuarioService {
    UsuarioRepository rep = new UsuarioRepository();
    public UsuarioSoulMove cadastrar(String nome, String email, String senha){

        try {
            return rep.cadastrar(nome, email, senha);
        } catch (SQLException e) {
            if (e.getErrorCode() == 1){
                System.out.println("ERRO, USUARIO");
            };
            return null;
        }
    }
}
