package br.com.soulmove.model.exceptions;

import br.com.soulmove.repository.OracleErrorParser;

import java.sql.SQLException;

public final class OracleExceptionTranslator {

    private OracleExceptionTranslator() {}

    /**
     * Examina a SQLException do Oracle e lança a exceção de domínio correspondente.
     */
    public static void translateException(SQLException e, String mensagemContexto)
            throws ConstraintViolationException, DatabaseException, NullDataException, TooLargeException {

        int errorCode = e.getErrorCode();

        switch (errorCode) {
            case 1: // ORA-00001: Unique Constraint
                String constraintUnique = OracleErrorParser.extrairNomeConstraint(e.getMessage());
                throw new ConstraintViolationException(
                        mensagemContexto + ": Registro duplicado para a regra " + constraintUnique,
                        constraintUnique,
                        e
                );

            case 1400: // ORA-01400: NOT NULL
                String colunaNull = OracleErrorParser.extrairNomeColuna(e.getMessage());
                throw new NullDataException(
                        mensagemContexto + ": O campo obrigatório '" + colunaNull + "' não foi informado.",
                        colunaNull,
                        e
                );

            case 2290: // ORA-02290: CHECK
                String constraintCheck = OracleErrorParser.extrairNomeConstraint(e.getMessage());
                throw new ConstraintViolationException(
                        mensagemContexto + ": Validação de regra violada (" + constraintCheck + ").",
                        constraintCheck,
                        e
                );

            case 2291: // ORA-02291: FK (Pai inexistente)
                String fkInexistente = OracleErrorParser.extrairNomeConstraint(e.getMessage());
                throw new ConstraintViolationException(
                        mensagemContexto + ": O registro pai associado não existe.",
                        fkInexistente,
                        e
                );

            case 2292: // ORA-02292: FK (Filhos vinculados)
                String fkFilhos = OracleErrorParser.extrairNomeConstraint(e.getMessage());
                throw new ConstraintViolationException(
                        mensagemContexto + ": Não é possível excluir pois existem registros vinculados.",
                        fkFilhos,
                        e
                );

            case 12899: // ORA-12899: Value Too Large
                String colunaTamanho = OracleErrorParser.extrairNomeColuna(e.getMessage());
                throw new TooLargeException(
                        mensagemContexto + ": Texto muito longo para o campo '" + colunaTamanho + "'.",
                        colunaTamanho,
                        e
                );

            default:
                // Erros de sintaxe, conexão ou outros códigos do Oracle
                throw new DatabaseException(mensagemContexto + ": Erro inesperado no banco de dados.", e);
        }
    }
}
