package br.com.soulmove.repository;

import br.com.soulmove.model.Conquista;
import br.com.soulmove.model.UsuarioSoulMove;
import br.com.soulmove.model.exceptions.*;
import br.com.soulmove.model.type.TipoMissao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class ConquistaRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Conquista> conquistaMapper = (rs, rowNum) ->{
        Conquista c = new Conquista();
        c.setId(rs.getLong("conquista_id"));
        c.setNome(rs.getString(rs.getString("nome")));
        c.setPontos(rs.getInt("pontos"));
        c.setTitulo(rs.getString("titulo"));
        c.setDescricao(rs.getString("descricao"));
        return c;
    };

    @Autowired
    public ConquistaRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public long cadastrar(int pontos, String nome, String titulo, String descricao){
        String sql = "INSERT INTO tb_conquista (pontos, nome, titulo, descricao) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"conquista_id"});
            ps.setInt(1, pontos);
            ps.setString(2, nome);
            ps.setString(3, titulo);
            ps.setString(4, descricao);
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();


    }

    public Conquista buscar(long id){
        String sql = "SELECT conquista_id, pontos, nome, titulo, descricao FROM tb_conquista WHERE conquista_id = ?";
        return jdbcTemplate.queryForObject(sql, conquistaMapper, id);

    }

    public List<Conquista> buscarTodas(){
        String sql = "SELECT conquista_id, pontos, nome, titulo, descricao FROM tb_conquista";
        return jdbcTemplate.query(sql, conquistaMapper);
    }

    public int excluir(long id){
        String sql = "DELETE FROM tb_conquista WHERE conquista_id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public int editar(long id, int pontos, String nome, String titulo, String descricao){
        String sql = "UPDATE tb_conquista SET pontos = ?, nome = ?, titulo = ?, descricao = ? WHERE conquista_id = ?";
        return jdbcTemplate.update(sql, pontos, nome, titulo, descricao, id);
    }

    public List<Conquista> buscarConcluidas(long usuarioId){
        String sql = "SELECT conquista_id, pontos, titulo, nome, descricao FROM tb_conquista WHERE conquista_id IN (SELECT conquista_id FROM tb_usuario_conquista WHERE usuario_id = ?)";
        return jdbcTemplate.query(sql, conquistaMapper, usuarioId);
    }

    public void completar(UsuarioSoulMove usuario, Conquista conquista){

        String sql = "INSERT INTO tb_usuario_conquista (usuario_id, conquista_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, usuario.getId(), conquista.getId());
    }

    public void excluirDependencias(Conquista conquista) throws ConstraintViolationException, NullDataException, DatabaseException, TooLargeException, UnableToFindEntityException {
        String sql = "DELETE FROM tb_usuario_conquista WHERE conquista_id = ?";
        String sql2 = "UPDATE tb_usuario SET titulo_atual = null WHERE titulo_atual = ?";

        jdbcTemplate.update(sql, conquista.getId());
        jdbcTemplate.update(sql2, conquista.getId());
    }
}
