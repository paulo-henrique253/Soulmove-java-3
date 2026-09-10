package br.com.soulmove.repository;

import br.com.soulmove.model.Conquista;
import br.com.soulmove.model.UsuarioSoulMove;
import br.com.soulmove.model.exceptions.ConstraintViolationException;
import br.com.soulmove.model.exceptions.DatabaseException;
import br.com.soulmove.model.exceptions.NullDataException;
import br.com.soulmove.model.exceptions.TooLargeException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ConquistaRepository {

    public Conquista cadastrar(int pontos, String nome, String titulo, String descricao) throws Exception{
        String sql = "INSERT INTO tb_conquista (pontos, nome, titulo, descricao) VALUES (?, ?, ?, ?)";
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)){

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
            switch (e.getErrorCode()) {
                case 1: //Não vai acontecer
                    throw new ConstraintViolationException("Restrição violada.", OracleErrorParser.extrairNomeConstraint(e.getMessage()), e);

                case 1400:
                    throw new NullDataException("Valor nulo inserido em campo obrigatório.", OracleErrorParser.extrairNomeColuna(e.getMessage()), e);

                case 12899:
                    throw new TooLargeException("Valor excedeu o limite de caracteres.", OracleErrorParser.extrairNomeConstraint(e.getMessage()), e);
                default:
                    throw new DatabaseException("Erro inesperado no banco de dados.", e);

            }
        }
    }

    public List<Conquista> buscarTodas() throws Exception{
        String sql = "SELECT * FROM tb_conquista";
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

        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public int excluir(Conquista conquista)throws Exception {
        String sql = "DELETE * FROM tb_conquista WHERE id = ?";
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            int registros = 0;

            pstmt.setLong(1, conquista.getId());
            registros = pstmt.executeUpdate();


            return registros;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


}
