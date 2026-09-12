package br.com.soulmove.app;

import br.com.soulmove.Service.UsuarioService;
import br.com.soulmove.model.exceptions.ConstraintViolationException;
import br.com.soulmove.model.exceptions.InvalidDataException;

import java.util.Scanner;

public class SoulMove {

    public static void main(String[] args) {
        UsuarioService usuarioService = new UsuarioService();

        Scanner leitura = new Scanner(System.in);

        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n");
            System.out.println("""
                    ✮⋆˙ Escolha uma das opções abaixo:
                    
                        ⋮ ⌗ ┆ 1. Cadastrar usuário.
                        ⋮ ⌗ ┆ 2. Verificar Dados.
                        ⋮ ⌗ ┆ 3. Calcular emissão.
                        ⋮ ⌗ ┆ 4. Converter pontos.
                        ⋮ ⌗ ┆ 5. Verificar missões.
                        ⋮ ⌗ ┆ 6. Simular viajem.
                        ⋮ ⌗ ┆ 7. Ver histórico.
                        ⋮ ⌗ ┆ 0. SAIR DO PROGRAMA.
                    """);

            System.out.print("Insira a opção: ");
            opcao = leitura.nextInt();

            switch (opcao) {

                case 1:
                    System.out.println("\n" + "- - - Cadastrar usuário - - -" + "\n");

                    System.out.println("Insira seu nome");
                    String nome = leitura.next() + leitura.nextLine();

                    System.out.println("Insira seu email");
                    String email = leitura.nextLine();

                    System.out.println("Insira sua senha 👀");
                    String senha = leitura.nextLine();

                    try {
                        usuarioService.cadastrar(nome, email, senha);
                    } catch (ConstraintViolationException e){
                        System.out.println(e.getMessage());
                        if (e.getConstraintName().equalsIgnoreCase("TB_USUARIO_PK"))
                            System.out.println("Email ja cadastrado, insira outro");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }

                    break;
                case 2:

                case 3:
                    System.out.println("\n" + "- - - Verificar dados - - -" + "\n");

                    break;

                case 4:
                    System.out.println("\n" + "- - - Calcular emissão - - -" + "\n");

                    break;

                case 5:
                    System.out.println("\n" + "- - - Converter pontos - - -" + "\n");

                    break;

                case 6:
                    System.out.println("\n" + "- - - Verificar missões - - -" + "\n");

                    break;

                case 7:
                    System.out.println("\n" + "- - - Simular viajem - - -" + "\n");

                    break;

                case 8:
                    System.out.println("\n" + "- - - Ver histórico - - -" + "\n");

                    break;

                case 0:
                    System.out.println("\n" + "- - - Saindo do programa - - -" + "\n");
                    break;

                default:
                    System.out.println("\n" + "- - - Opção inválida - - - " + "\n");
                    break;
            }
        }
    }








}