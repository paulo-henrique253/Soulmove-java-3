package br.com.soulmove.repository;

import br.com.soulmove.model.Conquista;
import br.com.soulmove.model.TipoConquista;
import br.com.soulmove.model.UsuarioSoulMove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class ConquistaRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Conquista> conquistaMapper = (rs, rowNum) -> {
        Conquista c = new Conquista();
        c.setId(rs.getLong("conquista_id"));
        c.setNome(rs.getString("nome"));
        c.setPontos(rs.getInt("pontos"));
        c.setTitulo(rs.getString("titulo"));
        c.setDescricao(rs.getString("descricao"));
        c.setMeta(rs.getDouble("meta"));

        TipoConquista t = new TipoConquista();
        t.setId(rs.getLong("tipo_conquista_id"));
        t.setNome(rs.getString("tipo_nome"));
        t.setDescricao(rs.getString("tipo_descricao"));
        t.setUnidadeMedida(rs.getString("unidade_medida"));

        c.setTipo(t);
        return c;
    };

    @Autowired
    public ConquistaRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public long cadastrar(int pontos, String nome, String titulo, String descricao,double meta, String nomeTipo){
        String sql = "INSERT INTO tb_conquista (pontos, nome, titulo, descricao, tipo_missao_id) VALUES (?, ?, ?, ?, (SELECT tipo_conquista_id FROM tb_tipo_conquista WHERE nome = ?)))";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"conquista_id"});
            ps.setInt(1, pontos);
            ps.setString(2, nome);
            ps.setString(3, titulo);
            ps.setString(4, descricao);
            ps.setString(5, nomeTipo);
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();


    }

    public Conquista buscar(long id){
        String sql = """
                SELECT
                    c.conquista_id, c.pontos, c.nome, c.titulo, c.descricao, c.meta,
                    t.tipo_conquista_id, t.nome AS tipo_nome, t.descricao AS tipo_descricao, t.unidade_medida
                FROM tb_conquista c
                INNER JOIN tb_tipo_conquista t ON t.tipo_conquista_id = c.tipo_conquista_id
                WHERE c.conquista_id = ?
                """;
        return jdbcTemplate.queryForObject(sql, conquistaMapper, id);

    }

    public List<Conquista> buscarTodas(){
        String sql = """
                SELECT
                    c.conquista_id, c.pontos, c.nome, c.titulo, c.descricao, c.meta,
                    t.tipo_conquista_id, t.nome AS tipo_nome, t.descricao AS tipo_descricao, t.unidade_medida
                FROM tb_conquista c
                INNER JOIN tb_tipo_conquista t ON t.tipo_conquista_id = c.tipo_conquista_id
                """;
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
        String sql = """
                SELECT
                    c.conquista_id, c.pontos, c.nome, c.titulo, c.descricao, c.meta,
                    t.tipo_conquista_id, t.nome AS tipo_nome, t.descricao AS tipo_descricao, t.unidade_medida
                FROM tb_conquista c
                INNER JOIN tb_tipo_conquista t ON t.tipo_conquista_id = c.tipo_conquista_id
                WHERE c.conquista_id IN (SELECT DISTINCT conquista_id FROM tb_usuario_conquista WHERE usuario_id = ?)                
                """;
        return jdbcTemplate.query(sql, conquistaMapper, usuarioId);
    }

    public void completar(UsuarioSoulMove usuario, Conquista conquista){

        String sql = "INSERT INTO tb_usuario_conquista (usuario_id, conquista_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, usuario.getId(), conquista.getId());
    }

    public void excluirDependencias(long conquistaId){
        String sql = "DELETE FROM tb_usuario_conquista WHERE conquista_id = ?";
        String sql2 = "UPDATE tb_usuario SET titulo_atual = null WHERE titulo_atual = ?";

        jdbcTemplate.update(sql, conquistaId);
        jdbcTemplate.update(sql2, conquistaId);
    }
}
