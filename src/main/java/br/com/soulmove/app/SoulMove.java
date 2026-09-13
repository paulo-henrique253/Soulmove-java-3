package br.com.soulmove.app;

import br.com.soulmove.Service.*;
import br.com.soulmove.api.CalculadorDeRotas;
import br.com.soulmove.model.Conquista;
import br.com.soulmove.model.Missao;
import br.com.soulmove.model.UsuarioSoulMove;
import br.com.soulmove.model.Viagem;
import br.com.soulmove.model.exceptions.*;
import br.com.soulmove.model.type.TipoMissao;
import br.com.soulmove.model.type.Veiculo;

import java.util.List;
import java.util.Scanner;

public class SoulMove {

    public static void main(String[] args) {
        UsuarioService usuarioService = new UsuarioService();
        MissaoService missaoService = new MissaoService();
        ConquistaService conquistaService = new ConquistaService();
        ViagemService viagemService = new ViagemService();
        CarteiraService carteiraService = new CarteiraService();

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
                    catch (UnableToFindEntityException e){
                        System.out.println(e.getMessage());
                        System.out.println("Email não cadastrado!\n");
                    }
                    catch (Exception e) {
                        System.out.println(e.getMessage());
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
                    }
                    catch (ConstraintViolationException e){

                        System.out.println(e.getMessage());

                        if (e.getConstraintName().equalsIgnoreCase("TB_USUARIO_UK"))
                            System.out.println("ERRO!Email ja cadastrado, insira outro");
                    }
                    catch (TooLargeException e){

                        System.out.println("ERRO!O campo \"" + e.getColumnName().toLowerCase() + "\" Não pode ser tão grande!");
                    }
                    catch (Exception e) {

                        System.out.println(e.getMessage());
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
                        ⋮ ⌗ ┆ 10. Adicionar itulo ao perfil
                        ⋮ ⌗ ┆ 11. Converter Pontos
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
                    }
                    catch (ConstraintViolationException e){

                        System.out.println(e.getMessage());

                        if (e.getConstraintName().equalsIgnoreCase("TB_USUARIO_UK"))
                            System.out.println("ERRO!Email ja cadastrado, insira outro");
                    }
                    catch (TooLargeException e){

                        System.out.println("ERRO!O campo \"" + e.getColumnName().toLowerCase() + "\" Não pode ser tão grande!");
                    }
                    catch (Exception e) {

                        System.out.println(e.getMessage());
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

                    }
                    catch (UnableToFindEntityException e){

                        System.out.println("Email não cadastrado!");
                    }
                    catch (Exception e) {
                        System.out.println(e.getMessage());
                    }

                }

                case 3 -> {
                    System.out.println("\n" + "- - - Vizualizar Perfil - - -" + "\n");
                    try {
                        System.out.println(usuarioAtual);
                        System.out.println("Saldo: " + carteiraService.buscar(usuarioAtual).getSaldo());
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

                        viagemService.viajar(origem, destino, Veiculo.getTipoVeiculo(veiculo), kmPercorridos, carbonoEconomizado, carbonoEmitido, usuarioAtual);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                }

                case 5 ->{
                    System.out.println("\n" + "- - - Ver Historico de Viagens - - -" + "\n");
                    try {
                        List<Viagem> viagens = viagemService.obterHistorico(usuarioAtual);
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
                        missaoService.completarMissao(usuarioAtual, missao);
                    }
                    catch (UnableToFindEntityException e){
                        if(e.getTableName().equalsIgnoreCase("TB_MISSAO")){
                            System.out.println("Id inválido!");
                        } else System.out.println(e.getMessage());
                    }
                    catch (ConstraintViolationException e){
                        if (e.getConstraintName().equalsIgnoreCase("TB_USUARIO_MISSAO_PK")){
                            System.out.println("Missão ja concluida");
                        } else System.out.println(e.getMessage());
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
                    }catch (UnableToFindEntityException e){
                        if(e.getTableName().equalsIgnoreCase("TB_CONQUISTA"))
                            System.out.println("Id Inválido");
                        else
                            System.out.println(e.getMessage());
                    }
                    catch (ConstraintViolationException e){
                        if (e.getConstraintName().equalsIgnoreCase("TB_USUARIO_CONQUISTA_PK")){
                            System.out.println("conquista ja concluida");
                        } else System.out.println(e.getMessage());
                    }
                    catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

                case 10 ->{
                    System.out.println("\n" + "- - - Adicionar titulo ao perfil - - -" + "\n");
                    try {
                        List<Conquista> conquistasConcluidas = conquistaService.buscarConcluidas(usuarioAtual);
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
                    } catch (UnableToFindEntityException e){
                        if(e.getTableName().equalsIgnoreCase("TB_CONQUISTA"))
                            System.out.println("Id inválido");
                        else System.out.println(e.getMessage());
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
                        System.out.println("ERRO! Você não possui pontos o bastante!");
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
                                TipoMissao.valueOf(tipo);
                            } catch (IllegalArgumentException e){
                                tipo = "ERRO";
                                System.out.println("Valor inválido");
                            }
                        }while (tipo == "ERRO");

                        System.out.println("Insira a descrição: ");
                        String descricao = leitura.nextLine();


                        missaoService.cadastrar(pontos, nome, TipoMissao.valueOf(tipo), descricao);
                    }catch (TooLargeException e) {
                        System.out.println("O campo \"" + e.getColumnName().toLowerCase() +"\" não pode ser tão grande!");
                    } catch (Exception e) {
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
                                TipoMissao.valueOf(tipo);
                            } catch (IllegalArgumentException e){
                                tipo = "ERRO";
                                System.out.println("Valor inválido");
                            }
                        }while (tipo == "ERRO");

                        System.out.println("Insira a descrição: ");
                        String descricao = leitura.nextLine();


                        missaoService.editar(id, new Missao(id, nome, TipoMissao.valueOf(tipo), descricao, pontos));
                    } catch (UnableToFindEntityException e){
                        if (e.getTableName().equalsIgnoreCase("TB_MISSAO"))
                            System.out.println("Id inválido");
                    }
                    catch (TooLargeException e) {
                        System.out.println("O campo \"" + e.getColumnName().toLowerCase() +"\" não pode ser tão grande!");
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
                            missaoService.excluir(missao);
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
                    }catch (TooLargeException e){
                        System.out.println("O campo \"" +e.getColumnName()+ "\" Não pode ser tão grande!" );
                    } catch (Exception e) {
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

                        conquistaService.editar(id, new Conquista(id, nome, descricao, pontos, titulo));
                    }catch (UnableToFindEntityException e){
                        System.out.println("id inválido");
                    }
                    catch (TooLargeException e){
                        System.out.println("O campo \"" +e.getColumnName()+ "\" Não pode ser tão grande!" );
                    } catch (Exception e) {
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
                        String resp = leitura.nextLine();
                        if (resp.equalsIgnoreCase("s"))
                            conquistaService.excluir(conquista);
                        else System.out.println("Exclusão cancelada");


                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }

                case 0 -> {
                    System.out.println("\n" + "- - - Saindo do programa - - -" + "\n");


                }

                default -> {
                    System.out.println("\n" + "- - - Opção inválida - - - " + "\n");

                }


            }

            System.out.println("Aperte ENTER para continuar");
            leitura.next();
        }
    }
}