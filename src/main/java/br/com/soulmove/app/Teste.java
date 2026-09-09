package br.com.soulmove.app;

import br.com.soulmove.model.Usuario;
import br.com.soulmove.repository.ConquistaRepository;
import br.com.soulmove.repository.UsuarioRepository;

public class Teste {
    public static void main(String[] args) throws Exception {
        ConquistaRepository cr = new ConquistaRepository();

        cr.cadastrar(67, "Menos emissao, mais goon", "Gooner", "Nessa missão, Goone bastante pra reduzir a emissao e tals");
    }
}
