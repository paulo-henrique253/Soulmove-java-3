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

import java.sql.Date;
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

    private final RowMapper<Conquista> conquistaUsuarioMapper = (rs, rowNum) -> {
        Conquista c = new Conquista();
        c.setId(rs.getLong("conquista_id"));
        c.setNome(rs.getString("nome"));
        c.setPontos(rs.getInt("pontos"));
        c.setTitulo(rs.getString("titulo"));
        c.setDescricao(rs.getString("descricao"));
        c.setMeta(rs.getDouble("meta"));
        Date dataConclusao = rs.getObject("data_conquista", Date.class);
        if (dataConclusao != null)
            c.setDataConclusao(rs.getDate("data_conquista").toLocalDate());


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
        String sql = """
                INSERT INTO TB_CONQUISTA (tipo_conquista_id, meta ,nome, titulo, descricao, pontos)
                    VALUES ((SELECT tipo_conquista_id FROM TB_TIPO_CONQUISTA WHERE nome = ?),
                            ?, ?, ?, ?, ?)
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"conquista_id"});

            ps.setString(1, nomeTipo);
            ps.setDouble(2, meta);
            ps.setString(3, nome);
            ps.setString(4, titulo);
            ps.setString(5, descricao);
            ps.setInt(6, pontos);

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

    public List<Conquista> buscarTodas(long usuarioId){
        String sql = """
                SELECT
                    c.conquista_id, c.pontos, c.nome, c.titulo, c.descricao, c.meta, uc.data_conquista,
                    t.tipo_conquista_id, t.nome AS tipo_nome, t.descricao AS tipo_descricao, t.unidade_medida
                FROM tb_conquista c
                LEFT JOIN tb_tipo_conquista t ON t.tipo_conquista_id = c.tipo_conquista_id
                LEFT JOIN tb_usuario_conquista uc ON uc.conquista_id = c.conquista_id AND uc.usuario_id = ?
                """;
        return jdbcTemplate.query(sql, conquistaUsuarioMapper, usuarioId);
    }

    public int excluir(long id){
        String sql = "DELETE FROM tb_conquista WHERE conquista_id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public int editar(long id, int pontos, String nome, String titulo, String descricao, double meta, String nomeTipo){
        String sql = "UPDATE tb_conquista SET pontos = ?, nome = ?, titulo = ?, descricao = ?, meta = ?, tipo_conquista_id = (SELECT tipo_conquista_id FROM tb_tipo_conquista WHERE nome = ?) WHERE conquista_id = ?";
        return jdbcTemplate.update(sql, pontos, nome, titulo, descricao, meta, nomeTipo, id);
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

    public List<Conquista> buscarPendentes(long usuarioId){
        String sql = """
                SELECT
                    c.conquista_id, c.pontos, c.nome, c.titulo, c.descricao, c.meta,
                    t.tipo_conquista_id, t.nome AS tipo_nome, t.descricao AS tipo_descricao, t.unidade_medida
                FROM tb_conquista c
                INNER JOIN tb_tipo_conquista t ON t.tipo_conquista_id = c.tipo_conquista_id
                WHERE c.conquista_id NOT IN (SELECT DISTINCT conquista_id FROM tb_usuario_conquista WHERE usuario_id = ?)                
                """;
        return jdbcTemplate.query(sql, conquistaMapper, usuarioId);
    }


    public void completar(long usuarioId, long conquistaId){

        String sql = "INSERT INTO tb_usuario_conquista (usuario_id, conquista_id, pontuacao_recebida, flag_titulo_atual) VALUES (?, ?, (SELECT pontos FROM tb_conquista WHERE conquista_id = ?), 'N')";
        jdbcTemplate.update(sql, usuarioId, conquistaId, conquistaId);
    }

    public void excluirDependencias(long conquistaId){
        String sql = "DELETE FROM tb_usuario_conquista WHERE conquista_id = ?";

        jdbcTemplate.update(sql, conquistaId);
    }
}
