package br.com.soulmove.repository;


import br.com.soulmove.model.Missao;
import br.com.soulmove.model.UsuarioSoulMove;
import br.com.soulmove.model.exceptions.*;
import br.com.soulmove.model.type.StatusMissao;
import br.com.soulmove.model.type.TipoMissao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MissaoRepository {

    public Missao cadastrar(int pontos, String nome, TipoMissao tipo, String descricao)
            throws DatabaseException, ConstraintViolationException, NullDataException, TooLargeException {
        String sql = "INSERT INTO tb_missao (pontos_missao, titulo, tipo_missao, descricao) VALUES (?, ?, ?, ?)";
        try (Connection con = new ConnectionFactory().getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, pontos);
            pstmt.setString(2, nome);
            pstmt.setString(3, tipo.getTipo());
            pstmt.setString(4, descricao);

            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                long id = rs.getBigDecimal(1).longValue();
                return new Missao(id, nome, tipo, descricao, pontos);
            }

            return null;
        } catch (SQLException e) {
            OracleExceptionTranslator.translateException(e, "Erro ao cadastrar missao");
            return null;
        }
    }

    public List<Missao> buscarTodas()
            throws DatabaseException, ConstraintViolationException, NullDataException, TooLargeException {
        String sql = "SELECT missao_id, pontos_missao, titulo, tipo_missao, descricao FROM tb_missao";
        try (Connection con = new ConnectionFactory().getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {
            List<Missao> missoes = new ArrayList<>();

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                long id = rs.getBigDecimal("missao_id").longValue();
                int pontos = rs.getInt("pontos_missao");
                String titulo = rs.getString("titulo");
                TipoMissao tipo = TipoMissao.getTipoMissao(rs.getString("tipo_missao"));
                String descricao = rs.getString("descricao");

                Missao missao = new Missao(id, titulo, tipo, descricao, pontos);
                missoes.add(missao);
            }


            return missoes;

        } catch (SQLException e) {
            OracleExceptionTranslator.translateException(e, "Erro ao buscar missões");
            return null;
        }
    }

    public Missao buscar(long id)
            throws DatabaseException, ConstraintViolationException, NullDataException, TooLargeException, UnableToFindEntityException {
        String sql = "SELECT pontos_missao, titulo, tipo_missao, descricao FROM tb_missao WHERE missao_id = ?";
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                int pontos = rs.getInt("pontos_missao");
                String titulo = rs.getString("titulo");
                TipoMissao tipo = TipoMissao.getTipoMissao(rs.getString("tipo_missao"));
                String descricao = rs.getString("descricao");
                return new Missao(id, titulo, tipo, descricao, pontos);

            } else {
                throw new UnableToFindEntityException("Erro ao buscar missão: entidade não encontrada", "TB_MISSAO");
            }

        }catch (SQLException e){
            OracleExceptionTranslator.translateException(e, "Erro ao buscar missão");
            return null;
        }
    }

    public int excluir(Missao missao)
            throws DatabaseException, ConstraintViolationException, NullDataException, TooLargeException, UnableToFindEntityException {
        String sql = "DELETE * FROM tb_missao WHERE id = ?";
        try (Connection con = new ConnectionFactory().getConnection();
                PreparedStatement pstmt = con.prepareStatement(sql)) {

            int registros = 0;

            pstmt.setLong(1, missao.getId());
            registros = pstmt.executeUpdate();

            if (registros == 0)
                throw new UnableToFindEntityException("Erro ao excluir: Entidade não encontrada", "TB_MISSAO");
            return registros;

        } catch (SQLException e) {
            OracleExceptionTranslator.translateException(e, "Erro ao excluir missão");
            return -1;
        }
    }

    public int editar(long id, Missao missao)
            throws DatabaseException, ConstraintViolationException, NullDataException, TooLargeException, UnableToFindEntityException {
        String sql = "UPDATE tb_missao SET pontos_missao = ?, titulo = ?, descricao = ?, tipo_missao = ? WHERE id = ?";
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)){

            pstmt.setInt(1, missao.getPontos());
            pstmt.setString(2, missao.getTitulo());
            pstmt.setString(3, missao.getDescricao());
            pstmt.setString(4, missao.getTipo().getTipo());
            pstmt.setLong(5, missao.getId());

            int registros = pstmt.executeUpdate();
            if (registros == 0)
                throw new UnableToFindEntityException("Erro ao editar missao: entidade não encontrada", "TB_MISSAO");

            return registros;

        } catch (SQLException e){
            OracleExceptionTranslator.translateException(e, "Erro ao editar missao");
            return -1;
        }
    }


    public List<Missao> buscarConcluidas(UsuarioSoulMove usuario)
        throws DatabaseException, ConstraintViolationException, NullDataException, TooLargeException{
        String sql = "SELECT missao_id, pontos_missao, titulo, tipo_missao, descricao FROM tb_missao WHERE missao_id IN (SELECT missao_id FROM tb_usuario_missao WHERE usuario_id = ? and status_missao = 'concluida')";
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)){
            pstmt.setLong(1, usuario.getId());
            ResultSet rs = pstmt.executeQuery();
            List<Missao> missoes = new ArrayList<>();
            while (rs.next()) {
                long id = rs.getBigDecimal("missao_id").longValue();
                int pontos = rs.getInt("pontos_missao");
                String titulo = rs.getString("titulo");
                TipoMissao tipo = TipoMissao.getTipoMissao(rs.getString("tipo_missao"));
                String descricao = rs.getString("descricao");

                Missao missao = new Missao(id, titulo, tipo, descricao, pontos);
                missoes.add(missao);
            }
            return missoes;
        } catch (SQLException e){
            OracleExceptionTranslator.translateException(e, "Erro ao buscar missões.");
            return null;
        }
    }

    public void completar(UsuarioSoulMove usuario, Missao missao)
    throws DatabaseException, ConstraintViolationException, NullDataException, TooLargeException{
        String sql = "INSERT INTO tb_usuario_missao (status_missao, pontuacao_recebida, usuario_id, missao_id) ";
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)){

            pstmt.setString(1,StatusMissao.CONCLUIDA.getStatus());
            pstmt.setInt(2, missao.getPontos());
            pstmt.setLong(3, usuario.getId());
            pstmt.setLong(4, missao.getId());

        }catch (SQLException e){
            OracleExceptionTranslator.translateException(e, "Erro ao completar missao");
        }
    }

}
