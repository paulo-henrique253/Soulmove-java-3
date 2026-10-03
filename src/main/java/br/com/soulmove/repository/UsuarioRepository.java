package br.com.soulmove.repository;

import br.com.soulmove.model.Conquista;
import br.com.soulmove.model.Usuario;
import br.com.soulmove.model.UsuarioSoulMove;
import br.com.soulmove.model.exceptions.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.sql.SQLException;
import java.util.Map;

public class UsuarioRepository {

    private final JdbcTemplate jdbcTemplate;

    public UsuarioRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    RowMapper<UsuarioSoulMove> usuarioMapper = (rs, rowNum) ->{
        long usuarioId = rs.getLong("usuario_id");
        String usuarioNome = rs.getString("nome");
        String usuarioEmail = rs.getString("email");
        LocalDate usuarioDataCadastro = rs.getDate("data_cadastro").toLocalDate();
        String usuarioSenha = rs.getString("senha");
        int usuarioPontos = rs.getInt("pontos");

        UsuarioSoulMove u = new UsuarioSoulMove(usuarioId, usuarioNome, usuarioPontos , usuarioEmail, usuarioDataCadastro, usuarioSenha);


        //conquista
        Long conquistaId = rs.getObject("titulo_atual", Long.class);
        if (conquistaId != null){
            Conquista c = new Conquista();
            c.setId(conquistaId);
            c.setNome(rs.getString("conquista_nome"));
            c.setTitulo(rs.getString("conquista_titulo"));
            c.setDescricao(rs.getString("conquista_descricao"));
            c.setPontos(rs.getInt("conquista_pontos"));

            u.setTituloAtual(c);
        } else u.setTituloAtual(null);

        return u;
    };

    public UsuarioSoulMove cadastrar(String nome, String email, String senha){
        String sql = "INSERT INTO TB_USUARIO(nome, pontos, email, senha) VALUES(?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[] { "USUARIO_ID" , "DATA_CADASTRO"});
            ps.setString(1, nome);
            ps.setInt(2, 0);
            ps.setString(3, email);
            ps.setString(4, senha);

            return ps;
        }, keyHolder);

        Map<String, Object> chaves = keyHolder.getKeys();
        if (chaves != null && !chaves.isEmpty()){
            long id = ((Number) chaves.get("USUARIO_ID")).longValue();
            LocalDate data = ((Date) chaves.get("DATA_CADASTRO")).toLocalDate();


            UsuarioSoulMove u = new UsuarioSoulMove(id, nome, 0, email,data, senha);
            return u;
        }
        throw new RuntimeException("Impossivel obter as chaves geradas para o usuario");


    }

    public UsuarioSoulMove buscar(long id){
        String sql = """
            SELECT 
                u.usuario_id, u.nome, u.email, u.senha, u.data_cadastro, u.pontos, u.titulo_atual 
                c.nome AS conquista_nome, c.titulo AS conquista_titulo, c.descricao AS conquista_descricao, c.pontos AS conquista_pontos
            FROM tb_usuario u
            LEFT JOIN tb_conquista c ON u.titulo_atual = c.conquista_id
            WHERE u.usuario_id = ?
            """;
        return jdbcTemplate.queryForObject(sql, usuarioMapper, id);
    }

    public UsuarioSoulMove buscar(String email){
        String sql = """
            SELECT 
                u.usuario_id, u.nome, u.email, u.senha, u.data_cadastro, u.pontos, u.titulo_atual 
                c.nome AS conquista_nome, c.titulo AS conquista_titulo, c.descricao AS conquista_descricao, c.pontos AS conquista_pontos
            FROM tb_usuario u
            LEFT JOIN tb_conquista c ON u.titulo_atual = c.conquista_id
            WHERE u.email = ?
            """;
        return jdbcTemplate.queryForObject(sql, usuarioMapper, email);
    }

    public int alterarConquista(UsuarioSoulMove usuario, Conquista conquista){
        String sql = "UPDATE tb_usuario SET titulo_atual = ? WHERE usuario_id = ?";
        return jdbcTemplate.update(sql, conquista.getId(), usuario.getId());
    }

    public int excluir(UsuarioSoulMove usuario){
        String sql = "DELETE FROM tb_usuario WHERE id = ?";
        return jdbcTemplate.update(sql, usuario.getId());
    }


    public int alterarPontos(UsuarioSoulMove usuario, int pontos){
        String sql = "UPDATE tb_usuario SET pontos = ? WHERE usuario_id = ?";
        return jdbcTemplate.update(sql, pontos, usuario.getId());
    }

}
