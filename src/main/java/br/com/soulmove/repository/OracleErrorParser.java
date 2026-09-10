package br.com.soulmove.repository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class OracleErrorParser {

    // Regex para capturar o padrão "ESQUEMA"."TABELA"."COLUNA" do ORA-12899 e ORA-01400
    private static final Pattern COLUNA_PATTERN = Pattern.compile("\"[^\"]+\"\\.\"[^\"]+\"\\.\"([^\"]+)\"");

    // Construtor privado para evitar que a classe seja instanciada
    private OracleErrorParser() {}

    /**
     * Extrai o nome do atributo/coluna que violou a regra a partir da mensagem ORA-12899 ou ORA-01400.
     */
    public static String extrairNomeColuna(String mensagemOracle) {
        if (mensagemOracle == null) {
            return "COLUNA_DESCONHECIDA";
        }

        Matcher matcher = COLUNA_PATTERN.matcher(mensagemOracle);
        if (matcher.find()) {
            // O grupo 1 pega apenas o conteúdo das últimas aspas (o nome da coluna)
            return matcher.group(1);
        }

        return "COLUNA_DESCONHECIDA";
    }


    /**
     * Extrai o nome da constraint da mensagem de erro padronizada do Oracle.
     * Exemplo de mensagem: "ORA-00001: unique constraint (ESQUEMA.TB_USUARIO_UK) violated"
     */
    public static String extrairNomeConstraint(String mensagemOracle) {
        if (mensagemOracle == null) {
            return "UNKNOWN_CONSTRAINT";
        }

        int inicio = mensagemOracle.indexOf("(");
        int fim = mensagemOracle.indexOf(")");

        if (inicio != -1 && fim != -1 && fim > inicio) {
            String conteudo = mensagemOracle.substring(inicio + 1, fim);

            // Caso o Oracle retorne no formato "ESQUEMA.NOME_CONSTRAINT", remove o nome do esquema
            if (conteudo.contains(".")) {
                return conteudo.substring(conteudo.lastIndexOf(".") + 1);
            }
            return conteudo;
        }

        return "UNKNOWN_CONSTRAINT";
    }
}