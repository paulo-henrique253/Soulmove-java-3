package br.com.soulmove.repository;

import br.com.soulmove.model.Conquista;
import br.com.soulmove.model.UsuarioSoulMove;
import br.com.soulmove.model.exceptions.*;
import br.com.soulmove.model.type.TipoMissao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ConquistaRepository {

    public Conquista cadastrar(int pontos, String nome, String titulo, String descricao)
            throws DatabaseException, ConstraintViolationException, TooLargeException, NullDataException{
        String sql = "INSERT INTO tb_conquista (pontos, nome, titulo, descricao) VALUES (?, ?, ?, ?)";
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql, new String[] {"CONQUISTA_ID"})){

            pstmt.setInt(1, pontos);
            pstmt.setString(2, nome);
            pstmt.setString(3, titulo);
            pstmt.setString(4, descricao);

            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()){
                long id = rs.getBigDecimal(1).longValue();
                return new Conquista(id, nome, descricao, pontos, titulo);
            }

            return null;
        }catch (SQLException e){
            OracleExceptionTranslator.translateException(e, "Erro ao cadastrar missão");
            return null;
        }
    }

    public Conquista buscar(long id)
        throws DatabaseException, ConstraintViolationException, NullDataException, TooLargeException, UnableToFindEntityException {
        String sql = "SELECT pontos, nome, titulo, descricao FROM tb_conquista WHERE conquista_id = ?";
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)){

            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()){
                int pontos = rs.getInt("pontos");
                String nome = rs.getString("nome");
                String titulo = rs.getString("titulo");
                String descricao = rs.getString("descricao");

                return new Conquista(id, nome, descricao, pontos, titulo);

            }
            throw new UnableToFindEntityException("Conquista não encontrada");

        }catch (SQLException e){
            OracleExceptionTranslator.translateException(e, "Erro ao buscar conquista");
            return null;
        }

    }

    public List<Conquista> buscarTodas()
            throws DatabaseException, ConstraintViolationException, NullDataException, TooLargeException {
        String sql = "SELECT conquista_id, pontos, nome, titulo, descricao FROM tb_conquista";
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)){
            List<Conquista> conquistas = new ArrayList<>();

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                long id = rs.getBigDecimal("conquista_id").longValue();
                int pontos = rs.getInt("pontos");
                String nome = rs.getString("nome");
                String titulo = rs.getString("titulo");
                String descricao = rs.getString("descricao");

                Conquista conquista = new Conquista(id, nome, descricao, pontos, titulo);
                conquistas.add(conquista);
            }


            return conquistas;

        }catch (SQLException e){
            OracleExceptionTranslator.translateException(e, "Erro ao buscar conquistas");
            return null;
        }
    }

    public int excluir(Conquista conquista)
            throws DatabaseException, ConstraintViolationException,TooLargeException, NullDataException ,UnableToFindEntityException {
        String sql = "DELETE * FROM tb_conquista WHERE id = ?";
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            int registros = 0;

            pstmt.setLong(1, conquista.getId());
            registros = pstmt.executeUpdate();

            if (registros == 0 )
                throw new UnableToFindEntityException("Entidade não encontrada");

            return registros;

        } catch (SQLException e) {
            OracleExceptionTranslator.translateException(e, "Erro ao excluir conquista");
            return -1;
        }
    }

    public List<Conquista> buscarConcluidas(UsuarioSoulMove usuario)
            throws DatabaseException, ConstraintViolationException, NullDataException, TooLargeException{
        String sql = "SELECT conquista_id, pontos, titulo, nome, descricao FROM tb_conquista WHERE conquista_id IN (SELECT conquista_id FROM tb_usuario_conquista WHERE usuario_id = ?)";
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)){
            pstmt.setLong(1, usuario.getId());
            ResultSet rs = pstmt.executeQuery();

            List<Conquista> conquistas = new ArrayList<>();
            while (rs.next()) {
                long id = rs.getBigDecimal("conquista_id").longValue();
                int pontos = rs.getInt("pontos");
                String titulo = rs.getString("titulo");
                String nome = rs.getString("nome");
                String descricao = rs.getString("descricao");

                Conquista missao = new Conquista(id, nome,descricao, pontos, titulo);
                conquistas.add(missao);
            }
            return conquistas;
        } catch (SQLException e){
            OracleExceptionTranslator.translateException(e, "Erro ao buscar missões.");
            return null;
        }
    }


}
