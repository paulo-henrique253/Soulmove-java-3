# SoulMove

A SoulMove é uma solução de mobilidade sustentável integrada à
plataforma SoulUp. O projeto incentiva o uso do transporte público por
meio de missões, conquistas e recompensas em pontos, que podem ser
convertidos em saldo para recarga do Bilhete Único.

## Funcionalidades

-   Cadastro e login de usuários.
-   Perfil e carteira de mobilidade.
-   Simulação de trajetos e consulta ao histórico de viagens.
-   Calculadora de emissões de carbono para comparar meios de
    transporte.
-   Missões e conquistas com recompensas em pontos.
-   Conversão de pontos em saldo para recarga.
-   Cadastro, edição e exclusão de missões e conquistas.

## Tecnologias e requisitos

-   Java 21 (JDK 21)
-   Apache Maven
-   IDE com suporte a Java (IntelliJ IDEA, Eclipse ou VS Code)

As dependências são gerenciadas pelo Maven e baixadas durante a
importação ou compilação do projeto.

## Como executar

1.  Clone ou baixe este repositório.

2.  Abra a pasta raiz do projeto na sua IDE --- a pasta que contém o
    `pom.xml`.

3.  Aguarde a IDE importar o projeto e baixar as dependências.

4.  Execute a classe principal:

    `src/main/java/br/com/soulmove/SoulMove.java`

### Pelo terminal

Na pasta raiz do projeto, execute:

``` bash
mvn clean compile
mvn exec:java -Dexec.mainClass="br.com.soulmove.SoulMove"
```

## Banco de dados

As credenciais de acesso ao banco já estão configuradas no projeto para
fins acadêmicos. De acordo com a documentação, não é necessária uma
instalação ou configuração manual do banco para executar os testes.

## API

A API disponibiliza endpoints para usuários, carteira, missões,
conquistas, recargas e viagens.

**URL base:** https://soulmove-java-3.onrender.com/api/v1

Consulte os controllers do projeto para os detalhes de cada rota e seus
parâmetros.
