package br.com.soulmove.repository;

import br.com.soulmove.model.Conquista;
import br.com.soulmove.model.Usuario;
import br.com.soulmove.model.UsuarioSoulMove;
import br.com.soulmove.model.exceptions.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.sql.SQLException;

public class UsuarioRepository {


    public UsuarioSoulMove cadastrar(String nome, String email, String senha)throws ConstraintViolationException, NullDataException, TooLargeException, DatabaseException{
        String sql = "INSERT INTO TB_USUARIO(nome, pontos, email, data_cadastro, senha) VALUES(?, ?, ?, ?, ?)";

        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql, new String[] { "USUARIO_ID" })) {

            pstmt.setString(1, nome);
            pstmt.setInt(2, 0);
            pstmt.setString(3, email);

            LocalDate data = LocalDate.now();
            pstmt.setDate(4, Date.valueOf(data));

            pstmt.setString(5, senha);

            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()){
                long id = rs.getBigDecimal(1).longValue();
                int pontos = rs.getInt("pontos");
                return new UsuarioSoulMove(id, nome, pontos, email, data);
            }

            return null;
        } catch (SQLException e) {

            switch (e.getErrorCode()) {
                case 1:
                    throw new ConstraintViolationException("Restrição de chave única violada.", OracleErrorParser.extrairNomeConstraint(e.getMessage()), e);

                case 1400:
                    throw new NullDataException("Valor nulo inserido em campo obrigatório.", OracleErrorParser.extrairNomeColuna(e.getMessage()), e);

                case 12899:
                    throw new TooLargeException("Valor excedeu o limite de caracteres", OracleErrorParser.extrairNomeConstraint(e.getMessage()), e);
                default:
                    throw new DatabaseException("Erro inesperado no banco de dados.", e);

            }
        }
    }

    public UsuarioSoulMove buscar(long id) throws DatabaseException, UnableToFindEntityException{
        String sql = "SELECT * FROM tb_usuario WHERE id = ?";
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)){

            pstmt.setLong(1, id);

            return this.executarBusca(pstmt);

        } catch (SQLException e){

            throw new DatabaseException("Ocorreu um erro inesperado no banco de dados.",e);
        } catch (UnableToFindEntityException e){
            throw e;
        }
    }

    public UsuarioSoulMove buscar(String email) throws DatabaseException, UnableToFindEntityException{
        String sql = "SELECT * FROM tb_usuario WHERE email = ?";
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)){

            pstmt.setString(1, email);

            return this.executarBusca(pstmt);

        } catch (SQLException e){
            throw new DatabaseException("Ocorreu um erro inesperado no banco de dados.",e);
        } catch (UnableToFindEntityException e){
            throw e;
        }
    }

    public UsuarioSoulMove executarBusca(PreparedStatement pstmt) throws SQLException{
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()){
            long id = rs.getBigDecimal("usuario_id").longValue();
            String nome = rs.getString("nome");
            String email = rs.getString("email");
            String senha = rs.getString("senha");
            LocalDate data = rs.getDate("data_cadastro").toLocalDate();
            int pontos =rs.getInt("pontos");


            return new UsuarioSoulMove(id, nome, pontos, email , data);
        }
        else {
            throw new UnableToFindEntityException("Usuario Não encontrado.");
        }
    }

    public int alterarConquista(UsuarioSoulMove usuario, Conquista conquista)throws DatabaseException, ConstraintViolationException, UnableToFindEntityException{
        String sql = "UPDATE tb_usuario SET titulo_atual = ? WHERE usuario_id = ?";
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)){
            int registros = 0;

            pstmt.setLong(1, conquista.getId());
            pstmt.setLong(2, usuario.getId());

            registros = pstmt.executeUpdate();

            if (registros == 0)
                throw new UnableToFindEntityException("Usuario não encontrado");

            return registros;
        } catch (SQLException e){
            switch (e.getErrorCode()) {
                case 1:
                    throw new ConstraintViolationException("Restrição violada.", OracleErrorParser.extrairNomeConstraint(e.getMessage()), e);

                default:
                    throw new DatabaseException("Erro inesperado no banco de dados.", e);

            }
        }

    }

    public int excluir(UsuarioSoulMove usuario)throws DatabaseException, UnableToFindEntityException{
        String sql = "DELETE * FROM tb_usuario WHERE id = ?";
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)){

            int registros = 0;
            pstmt.setLong(1, usuario.getId());
            registros = pstmt.executeUpdate();

            if (registros == 0)
                throw new UnableToFindEntityException("Usuario não encontrado.");

            return registros;

        }catch (SQLException e){
            throw new DatabaseException("Ocorreu um erro inesperado no banco de dados.",e);
        }
    }
}
