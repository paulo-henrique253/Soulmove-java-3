package br.com.soulmove.app;

import br.com.soulmove.model.Conquista;
import br.com.soulmove.model.Usuario;
import br.com.soulmove.model.exceptions.ConstraintViolationException;
import br.com.soulmove.repository.ConquistaRepository;
import br.com.soulmove.repository.UsuarioRepository;

import java.util.List;

public class Teste {
    public static void main(String[] args) {
        ConquistaRepository cr = new ConquistaRepository();
        UsuarioRepository ur = new UsuarioRepository();

        try {
            ur.cadastrar("Enzo Zeni", "z@eni.com", "asdasdas");
        }catch (ConstraintViolationException e){
            System.out.println(e.getMessage());
            System.out.println(e.getConstraintName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
