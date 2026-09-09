package br.com.soulmove.repository;

import br.com.soulmove.model.Conquista;
import br.com.soulmove.model.Usuario;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class UsuarioRepository {


    public Usuario cadastrar(String nome, String email, String senha)throws Exception{
        String sql = "INSERT INTO TB_USUARIO(nome, pontos, email, data_cadastro, senha) VALUES(?, ?, ?, ?, ?)";

        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

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
                return new Usuario(id, nome, pontos, email, data);
            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Usuario buscar(long id) throws Exception{
        String sql = "SELECT * FROM tb_usuario WHERE id = ?";
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)){

            pstmt.setLong(1, id);

            return this.executarBusca(pstmt);
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public Usuario buscar(String email) throws Exception{
        String sql = "SELECT * FROM tb_usuario WHERE email = ?";
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)){

            pstmt.setString(1, email);

            return this.executarBusca(pstmt);
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public Usuario executarBusca(PreparedStatement pstmt) throws Exception{
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()){
            long id = rs.getBigDecimal("usuario_id").longValue();
            String nome = rs.getString("nome");
            String email = rs.getString("email");
            String senha = rs.getString("senha");
            LocalDate data = rs.getDate("data_cadastro").toLocalDate();
            int pontos =rs.getInt("pontos");


            return new Usuario(id, nome, pontos, email , data);
        }
        return  null;
    }

    public int alterarConquista(Usuario usuario, Conquista conquista){
        String sql = "UPDATE tb_usuario SET titulo_atual = ? WHERE usuario_id = ?";
        return 0;
    }
}
