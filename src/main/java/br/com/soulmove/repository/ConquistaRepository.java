package br.com.soulmove.repository;

import br.com.soulmove.model.Conquista;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

}
