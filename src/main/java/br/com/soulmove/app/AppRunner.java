package br.com.soulmove.app;

import br.com.soulmove.service.*;
import br.com.soulmove.api.CalculadorDeRotas;
import br.com.soulmove.model.Conquista;
import br.com.soulmove.model.Missao;
import br.com.soulmove.model.UsuarioSoulMove;
import br.com.soulmove.model.Viagem;
import br.com.soulmove.model.exceptions.InvalidDataException;
import br.com.soulmove.model.type.TipoMissao;
import br.com.soulmove.model.type.Veiculo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

public class AppRunner implements CommandLineRunner {

    private final UsuarioService usuarioService;
    private final MissaoService missaoService;
    private final ConquistaService conquistaService;
    private final ViagemService viagemService;
    private final CarteiraService carteiraService;

    public AppRunner(UsuarioService usuarioService, MissaoService missaoService, ConquistaService conquistaService, ViagemService viagemService, CarteiraService carteiraService) {
        this.usuarioService = usuarioService;
        this.missaoService = missaoService;
        this.conquistaService = conquistaService;
        this.viagemService = viagemService;
        this.carteiraService = carteiraService;
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void run(String... args) throws Exception {


        Scanner leitura = new Scanner(System.in);

        UsuarioSoulMove usuarioAtual = null;

        while (usuarioAtual == null){

            System.out.println("""
                    Deseja fazer login ou Criar uma nova conta?
                    1. Fazer login
                    2. Criar uma conta
                    """);
            System.out.println("Insira a opção");
            int opcao = leitura.nextInt();
            switch (opcao){
                case 1 -> {
                    System.out.println("Insira seu email: ");

                    String email = leitura.next() + leitura.nextLine();

                    System.out.println("Insira sua senha: ");
                    String senha = leitura.nextLine();

                    try {
                        usuarioAtual = usuarioService.logar(email, senha);

                    }
                    catch (RuntimeException e){
                        throw new RuntimeException(e);
                    }
                }


                case 2 ->{
                    System.out.println("Insira seu nome: ");

                    String nome = leitura.next() + leitura.nextLine();

                    System.out.println("Insira seu email: ");
                    String email = leitura.nextLine();

                    System.out.println("Insira sua senha: ");
                    String senha = leitura.nextLine();

                    try {
                        usuarioAtual = usuarioService.cadastrar(nome, email, senha);
                    } catch (RuntimeException e) {
                        throw new RuntimeException(e);
                    }
                }


            }

        }





        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n");
            System.out.println("""
                    ✮⋆˙ Escolha uma das opções abaixo:
                    
                        ⋮ ⌗ ┆ 1.  Cadastrar usuário.
                        ⋮ ⌗ ┆ 2.  Fazer login
                        ⋮ ⌗ ┆ 3.  Vizualizar perfil.
                        ⋮ ⌗ ┆ 4.  Simular viagem.
                        ⋮ ⌗ ┆ 5.  Ver histórico de viagens.
                        ⋮ ⌗ ┆ 6.  Verificar missões.
                        ⋮ ⌗ ┆ 7.  Completar missão.
                        ⋮ ⌗ ┆ 8.  Verificar conquistas.
                        ⋮ ⌗ ┆ 9.  Completar conquista
                        ⋮ ⌗ ┆ 10. Adicionar titulo ao perfil
                        ⋮ ⌗ ┆ 11. Converter Pontos
                        
                        ⋮ ⌗ ┆ 12. Cadastrar missao
                        ⋮ ⌗ ┆ 13. Editar missao
                        ⋮ ⌗ ┆ 14. Excluir missao
                        
                        ⋮ ⌗ ┆ 15. Cadastrar conquista
                        ⋮ ⌗ ┆ 16. Editar conquista
                        ⋮ ⌗ ┆ 17. Excluir conquista
                        ⋮ ⌗ ┆ 0.  SAIR DO PROGRAMA.
                    """);

            System.out.print("Insira a opção: ");
            opcao = leitura.nextInt();

            switch (opcao) {

                case 1 -> {
                    System.out.println("\n" + "- - - Cadastrar usuário - - -" + "\n");

                    System.out.println("Insira seu nome");

                    String nome = leitura.next() + leitura.nextLine();

                    System.out.println("Insira seu email");
                    String email = leitura.nextLine();

                    System.out.println("Insira sua senha 👀");
                    String senha = leitura.nextLine();

                    try {
                        usuarioAtual = usuarioService.cadastrar(nome, email, senha);
                    } catch (RuntimeException e) {
                        throw new RuntimeException(e);
                    }

                }

                case 2 -> {
                    System.out.println("\n" + "- - - Fazer Login - - -" + "\n");

                    System.out.println("Insira seu email: ");

                    String email = leitura.next() + leitura.nextLine();

                    System.out.println("Insira sua senha: ");
                    String senha = leitura.nextLine();

                    try {
                        usuarioAtual = usuarioService.logar(email, senha);

                    } catch (RuntimeException e) {
                        throw new RuntimeException(e);
                    }

                }

                case 3 -> {
                    System.out.println("\n" + "- - - Vizualizar Perfil - - -" + "\n");
                    try {
                        System.out.println(usuarioAtual);
                        System.out.println("Saldo: " + usuarioAtual.getCarteira().getSaldo());
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

                case 4 -> {
                    System.out.println("\n" + "- - - Simular viagem - - -" + "\n");

                    try {
                        System.out.println("Insira a origem da viagem. EXEMPLO: 'Avenida Paulista, 1000, São Paulo'");
                        String origem = leitura.next() + leitura.nextLine();

                        System.out.println("Insira o destino da viagem. EXEMPLO: 'Avenida Paulista, 1000, São Paulo'");
                        String destino = leitura.next() + leitura.nextLine();

                        System.out.println("\nBuscando as coordenadas...\n");
                        CalculadorDeRotas.Coordenadas coordenadasO = CalculadorDeRotas.buscarCoordenadas(origem);
                        CalculadorDeRotas.Coordenadas coordenadasD = CalculadorDeRotas.buscarCoordenadas(destino);

                        System.out.println("Calculando rotas...\n");
                        double kmPercorridos = CalculadorDeRotas.calcularRota(coordenadasO, coordenadasD);


                        String veiculo = "";

                        do {

                            for (Veiculo v : Veiculo.values()){
                                System.out.println(v.getVeiculo());
                            }
                            System.out.println("Insira o veículo:");
                            veiculo = leitura.nextLine();
                            try {
                                Veiculo.getTipoVeiculo(veiculo);
                            } catch (IllegalArgumentException e) {
                                veiculo = "ERRO";
                            }

                        }while (veiculo.equals("ERRO"));


                        double carbonoEmitido = kmPercorridos * Veiculo.getTipoVeiculo(veiculo).getEmissao();
                        double carbonoEconomizado = (Veiculo.CARRO.getEmissao() * kmPercorridos) - carbonoEmitido;

                        System.out.println(kmPercorridos);
                        System.out.println(carbonoEmitido);
                        System.out.println(Veiculo.CARRO.getEmissao());
                        System.out.println(Veiculo.getTipoVeiculo(veiculo).getEmissao());

                        viagemService.viajar(origem, destino, Veiculo.getTipoVeiculo(veiculo), kmPercorridos, carbonoEconomizado, carbonoEmitido, usuarioAtual);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }

                }

                case 5 ->{
                    System.out.println("\n" + "- - - Ver Historico de Viagens - - -" + "\n");
                    try {
                        List<Viagem> viagens = viagemService.obterHistorico(usuarioAtual.getId());
                        if (viagens.isEmpty())
                            System.out.println("Nenhuma viagem realizada");

                        else {
                            for (Viagem v : viagens){
                                System.out.println(v);
                                System.out.println("--------------------");
                            }
                        }

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

                case 6 -> {
                    System.out.println("\n" + "- - - Verificar missões - - -" + "\n");
                    try {
                        List<Missao> missoes = missaoService.buscarMissoes();
                        if (missoes.isEmpty()){
                            System.out.println("Nenhuma missão cadastrada!");
                        }
                        else {
                            for (Missao missao : missoes){
                                System.out.println(missao);
                                System.out.println("--------------------");
                            }
                        }
                    }
                    catch (Exception e){

                        System.out.println(e.getMessage());
                    }
                }

                case 7 ->{
                    System.out.println("\n" + "- - - Completar missão - - -" + "\n");

                    System.out.println("Insira o id da missão que deseja completar");
                    long idM = leitura.nextLong();
                    try {
                        Missao missao = missaoService.buscar(idM);
                        usuarioService.completarMissao(usuarioAtual, missao);
                    }
                    catch (Exception e) {
                        System.out.println(e.getMessage());
                    }

                }

                case 8 ->{
                    System.out.println("\n" + "- - - Verificar conquistas - - -" + "\n");
                    try {
                        List<Conquista> conquistas = conquistaService.buscarConquistas();
                        if (conquistas.isEmpty())
                            System.out.println("nenhuma conquista cadastrada");
                        else {
                            for (Conquista c : conquistas) {
                                System.out.println(c);
                                System.out.println("--------------------");
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

                case 9 ->{
                    System.out.println("\n" + "- - - Completar conquista - - -" + "\n");
                    try {
                        System.out.println("Insira o id da conquista: ");
                        long id = leitura.nextLong();
                        Conquista conquista = conquistaService.buscar(id);

                        usuarioService.completarConquista(usuarioAtual, conquista);
                    }catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

                case 10 ->{
                    System.out.println("\n" + "- - - Adicionar titulo ao perfil - - -" + "\n");
                    try {
                        List<Conquista> conquistasConcluidas = conquistaService.buscarConcluidas(usuarioAtual.getId());
                        if (conquistasConcluidas.isEmpty()){
                            System.out.println("Nenhuma conquista concluida");
                        } else {
                            for (Conquista c : conquistasConcluidas) {
                                System.out.println(c);
                                System.out.println("--------------------");
                            }
                            System.out.println("insira o id de uma conquista");
                            long id = leitura.nextLong();
                            Conquista conquista = conquistaService.buscar(id);
                            usuarioService.alterarTitulo(usuarioAtual, conquista);
                        }
                    }
                    catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }

                case 11 -> {
                    System.out.println("\n" + "- - - Converter pontos - - -" + "\n");
                    try {
                        System.out.println("Voce tem " + usuarioAtual.getPontos() + " pontos");
                        System.out.println("Insira a quantidade de pontos que deseja resgatar: ");
                        int quantidade = leitura.nextInt();
                        usuarioService.resgatarPontos(usuarioAtual, quantidade);
                    }
                    catch (InvalidDataException e){
                        System.out.println("ERRO! " + e.getMessage());
                    }
                    catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }

                // Funcionalidades de "adm"

                //missões
                case 12 -> {
                    System.out.println("\n" + "- - - Cadastrar missão - - -" + "\n");

                    try {
                        System.out.println("Insira os pontos:");
                        int pontos = leitura.nextInt();

                        System.out.println("insira o nome: ");
                        String nome = leitura.next() + leitura.nextLine();

                        String tipo = "";
                        do {
                            System.out.println("Tipos");
                            for (TipoMissao t : TipoMissao.values()){
                                System.out.println(t.getTipo());
                            }

                            System.out.println("\nInsira um tipo para a missao:");
                            try {
                                tipo = leitura.nextLine();
                                TipoMissao.getTipoMissao(tipo);
                            } catch (IllegalArgumentException e){
                                tipo = "ERRO";
                                System.out.println("Valor inválido");
                            }
                        }while (tipo == "ERRO");

                        System.out.println("Insira a descrição: ");
                        String descricao = leitura.nextLine();


                        missaoService.cadastrar(pontos, nome, TipoMissao.getTipoMissao(tipo), descricao);
                    }catch (Exception e) {
                        System.out.println(e.getMessage());
                    }

                }

                case 13 -> {
                    System.out.println("\n" + "- - - Editar missão - - -" + "\n");

                    try {
                        System.out.println("Insira o id da missão que deseja alterar: ");
                        long id = leitura.nextLong();

                        missaoService.buscar(id);

                        System.out.println("Insira os pontos:");
                        int pontos = leitura.nextInt();

                        System.out.println("insira o nome: ");
                        String nome = leitura.next() + leitura.nextLine();

                        String tipo = "";
                        do {
                            System.out.println("Tipos");
                            for (TipoMissao t : TipoMissao.values()){
                                System.out.println(t.getTipo());
                            }

                            System.out.println("\nInsira um tipo para a missao:");
                            try {
                                tipo = leitura.nextLine();
                                TipoMissao.getTipoMissao(tipo);
                            } catch (IllegalArgumentException e){
                                tipo = "ERRO";
                                System.out.println("Valor inválido");
                            }
                        }while (tipo == "ERRO");

                        System.out.println("Insira a descrição: ");
                        String descricao = leitura.nextLine();


                        missaoService.editar(id, pontos, nome, descricao,TipoMissao.getTipoMissao(tipo));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

                case 14 -> {
                    System.out.println("\n" + "- - - Excluir missão - - -" + "\n");

                    try {
                        System.out.println("Insira o id da missão que deseja excluir: ");
                        long id = leitura.nextLong();
                        Missao missao = missaoService.buscar(id);

                        System.out.println(missao + "\n--------------------\n");

                        System.out.println("Confirmar exclusão?(s/n)");
                        String resp = leitura.next() + leitura.nextLine();
                        if(resp.equalsIgnoreCase("s"))
                            missaoService.excluir(missao.getId());
                        else
                            System.out.println("Exclusão cancelada");

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }



                //conquistas
                case 15 -> {
                    System.out.println("\n" + "- - - Cadastrar conquista - - -" + "\n");
                    try {
                        System.out.println("Insira o nome da conquista: ");
                        String nome = leitura.next() + leitura.nextLine();

                        System.out.println("Insira os pontos: ");
                        int pontos = leitura.nextInt();

                        System.out.println("Insira o titulo atrelado á conquista: ");
                        String titulo = leitura.next() + leitura.nextLine();

                        System.out.println("Insira a descrição");
                        String descricao = leitura.nextLine();

                        conquistaService.cadastrar(pontos, nome, titulo, descricao);
                    }catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

                case 16 -> {
                    System.out.println("\n" + "- - - Editar conquista - - -" + "\n");
                    try {
                        System.out.println("Insira o id da conquista que deseja alterar");
                        long id = leitura.nextLong();

                        conquistaService.buscar(id);

                        System.out.println("Insira o nome da conquista: ");
                        String nome = leitura.next() + leitura.nextLine();

                        System.out.println("Insira os pontos: ");
                        int pontos = leitura.nextInt();

                        System.out.println("Insira o titulo atrelado á conquista: ");
                        String titulo = leitura.next() + leitura.nextLine();

                        System.out.println("Insira a descrição");
                        String descricao = leitura.nextLine();

                        conquistaService.editar(id, pontos, nome, titulo, descricao);
                    }catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

                case 17 -> {
                    System.out.println("\n" + "- - - Excluir conquista - - -" + "\n");

                    try {
                        System.out.println("Insira o id: ");
                        long id = leitura.nextLong();
                        Conquista conquista = conquistaService.buscar(id);

                        System.out.println(conquista + "\n--------------------\n");
                        System.out.println("Confirmar exclusão(s/n): ");
                        String resp = leitura.next() + leitura.nextLine();
                        if (resp.equalsIgnoreCase("s")){
                            conquistaService.excluir(conquista.getId());
                            usuarioAtual.setTituloAtual(null);
                        }
                        else System.out.println("Exclusão cancelada");


                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }

                case 0 -> {
                    System.out.println("\n" + "- - - Saindo do programa - - -" + "\n");
                    continue;


                }

                default -> {
                    System.out.println("\n" + "- - - Opção inválida - - - " + "\n");

                }


            }

            pausar();
        }
    }
    public static void pausar() {
        System.out.println("\nPressione ENTER para continuar...");

        Scanner leitura = new Scanner(System.in);
        // Se tem qualquer resíduo na linha atual do buffer (incluindo o \n anterior),
        if (leitura.hasNextLine()) {
            // Consome o que sobrou da linha atual, se houver
            String resto = leitura.nextLine();

        }
    }
}
