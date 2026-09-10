package br.com.soulmove.app;

import br.com.soulmove.model.Conquista;
import br.com.soulmove.model.Usuario;
import br.com.soulmove.repository.ConquistaRepository;
import br.com.soulmove.repository.UsuarioRepository;

import java.util.List;

public class Teste {
    public static void main(String[] args) throws Exception {
        ConquistaRepository cr = new ConquistaRepository();

        cr.cadastrar(67, "Enzo", "Do mau", "Place Holder Fachinelli");

        List<Conquista> conquistas = cr.buscarTodas();

        for (Conquista conquista : conquistas){
            System.out.println(conquista);
        }
    }
}
