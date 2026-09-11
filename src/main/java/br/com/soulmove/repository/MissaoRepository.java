package br.com.soulmove.repository;


import br.com.soulmove.model.Missao;
import br.com.soulmove.model.UsuarioSoulMove;
import br.com.soulmove.model.type.TipoMissao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MissaoRepository {

    public Missao cadastrar(int pontos, String nome, TipoMissao tipo, String descricao) throws Exception{
        String sql = "INSERT INTO tb_missao (pontos_missao, titulo, tipo_missao, descricao) VALUES (?, ?, ?, ?)";
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)){

            pstmt.setInt(1, pontos);
            pstmt.setString(2, nome);
            pstmt.setString(3, tipo.name());
            pstmt.setString(4, descricao);

            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()){
                long id = rs.getBigDecimal(1).longValue();
                return new Missao(id, nome, tipo, descricao, pontos);
            }

            return null;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public List<Missao> buscarTodas() throws Exception{
        String sql = "SELECT * FROM tb_missao";
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)){
            List<Missao> missoes = new ArrayList<>();

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                long id = rs.getBigDecimal("missao_id").longValue();
                int pontos = rs.getInt("pontos");
                String titulo = rs.getString("titulo");
                TipoMissao tipo = TipoMissao.valueOf(rs.getString("tipo_missao"));
                String descricao = rs.getString("descricao");

                Missao missao = new Missao(id, titulo, tipo, descricao, pontos);
                missoes.add(missao);
            }


            return missoes;

        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public List<Missao> buscarConcluidas(UsuarioSoulMove usuario){
        String sql = "SELECT * FROM tb_missao WHERE missao_id IN (SELECT missao_id FROM tb_usuario_missao WHERE usuario_id = ? and status_missao = 'concluida')";

    }



    public int excluir(Missao missao)throws Exception {
        String sql = "DELETE * FROM tb_missao WHERE id = ?";
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            int registros = 0;

            pstmt.setLong(1, missao.getId());
            registros = pstmt.executeUpdate();


            return registros;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
