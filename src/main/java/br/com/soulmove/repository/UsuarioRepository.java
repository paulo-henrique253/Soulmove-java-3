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


    public UsuarioSoulMove cadastrar(String nome, String email, String senha)
            throws ConstraintViolationException, NullDataException, TooLargeException, DatabaseException{
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
                return new UsuarioSoulMove(id, nome, 0, email, data, senha);
            }

            return null;
        } catch (SQLException e) {
            OracleExceptionTranslator.translateException(e, "Erro ao cadastrar usuário");
            return null;
        }
    }

    public UsuarioSoulMove buscar(long id) 
    throws DatabaseException, ConstraintViolationException, TooLargeException, NullDataException, UnableToFindEntityException{
        String sql = "SELECT usuario_id, nome, email, senha, data_cadastro, pontos, titulo_atual FROM tb_usuario WHERE usuario_id = ?";
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)){

            pstmt.setLong(1, id);

            return this.executarBusca(pstmt);

        } catch (SQLException e){

           OracleExceptionTranslator.translateException(e, "Erro ao buscar usuario: ");
           return null;
        } catch (UnableToFindEntityException e){
            throw e;
        }
    }

    public UsuarioSoulMove buscar(String email) throws DatabaseException, UnableToFindEntityException, ConstraintViolationException, NullDataException, TooLargeException {
        String sql = "SELECT usuario_id, nome, email, senha, data_cadastro, pontos, titulo_atual FROM tb_usuario WHERE email = ?";
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)){

            pstmt.setString(1, email);

            return this.executarBusca(pstmt);

        } catch (SQLException e){
            throw new DatabaseException("Ocorreu um erro inesperado no banco de dados.",e);
        } catch (Exception e){
            throw e;
        }
    }

    public UsuarioSoulMove executarBusca(PreparedStatement pstmt) throws SQLException, UnableToFindEntityException, ConstraintViolationException, NullDataException, DatabaseException, TooLargeException {
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()){
            long id = rs.getBigDecimal("usuario_id").longValue();
            String nome = rs.getString("nome");
            String email = rs.getString("email");
            String senha = rs.getString("senha");
            LocalDate data = rs.getDate("data_cadastro").toLocalDate();
            int pontos = rs.getInt("pontos");

            UsuarioSoulMove usuario = new UsuarioSoulMove(id, nome, pontos, email , data, senha);
            try {
                usuario.setTituloAtual(new ConquistaRepository().buscar(rs.getLong("titulo_atual")));
            } catch (UnableToFindEntityException e){
                usuario.setTituloAtual(null);
            }

            return usuario;
        }
        else {
            throw new UnableToFindEntityException("Usuario Não encontrado.", "TB_USUARIO");
        }
    }

    public int alterarConquista(UsuarioSoulMove usuario, Conquista conquista)
            throws DatabaseException, ConstraintViolationException, TooLargeException, NullDataException, UnableToFindEntityException{
        String sql = "UPDATE tb_usuario SET titulo_atual = ? WHERE usuario_id = ?";
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)){
            int registros = 0;

            pstmt.setLong(1, conquista.getId());
            pstmt.setLong(2, usuario.getId());

            registros = pstmt.executeUpdate();

            if (registros == 0)
                throw new UnableToFindEntityException("Usuario não encontrado", "TB_USUARIO");

            return registros;
        } catch (SQLException e){
            OracleExceptionTranslator.translateException(e, "Erro ao alterar conquista");
            return -1;
        }

    }

    public int excluir(UsuarioSoulMove usuario)
            throws DatabaseException, UnableToFindEntityException, TooLargeException, NullDataException, ConstraintViolationException{
        String sql = "DELETE * FROM tb_usuario WHERE id = ?";
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)){

            int registros = 0;
            pstmt.setLong(1, usuario.getId());
            registros = pstmt.executeUpdate();

            if (registros == 0)
                throw new UnableToFindEntityException("Usuario não encontrado.", "TB_USUARIO");

            return registros;

        }catch (SQLException e){
            OracleExceptionTranslator.translateException(e, "Erro ao excluir usuario");
            return -1;
        }
    }


    public void alterarPontos(UsuarioSoulMove usuario, int pontos) throws ConstraintViolationException, NullDataException, DatabaseException, TooLargeException, UnableToFindEntityException {
        String sql = "UPDATE tb_usuario SET pontos = ? WHERE usuario_id = ?";
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)){
            pstmt.setInt(1, pontos);
            pstmt.setLong(2, usuario.getId());

            int registros = pstmt.executeUpdate();
            if (registros == 0)
                throw new UnableToFindEntityException("Erro ao alterar pontos: entidade não encontrada", "TB_USUARIO");

        }catch (SQLException e){
            OracleExceptionTranslator.translateException(e, "Erro ao alterar pontos");
        }
    }
}
