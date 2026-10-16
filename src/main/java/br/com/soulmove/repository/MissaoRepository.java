package br.com.soulmove.repository;


import br.com.soulmove.exceptions.UnableToFindEntityException;
import br.com.soulmove.model.Missao;
import br.com.soulmove.model.UsuarioSoulMove;
import br.com.soulmove.model.type.StatusMissao;
import br.com.soulmove.model.TipoMissao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class MissaoRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MissaoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Missao> missaoMapper = (rs, numeroDaLinha) -> {
        Missao m = new Missao();
        m.setId(rs.getLong("missao_id"));
        m.setTitulo(rs.getString("titulo"));
        m.setDescricao(rs.getString("descricao"));
        m.setPontos(rs.getInt("pontos_missao"));
        m.setMeta(rs.getInt("meta_missao"));
        m.setPeriodicidade(rs.getInt("periodicidade_dias"));
        m.setPrazo(rs.getInt("prazo_dias"));
        m.setImagemId(rs.getLong("imagem_missao_id"));


        TipoMissao tipo = new TipoMissao();
        tipo.setId(rs.getLong("tipo_missao_id"));
        tipo.setNome(rs.getString("tipo_nome"));
        tipo.setDescricao(rs.getString("tipo_descricao"));
        tipo.setUnidadeMedida(rs.getString("tipo_unidade_medida"));
        m.setTipo(tipo);
        return m;
    };

    public long cadastrar(int pontos, String nome, TipoMissao tipo, String descricao, double meta, int periodicidade, int prazo, long imagemId){
        String sql = "INSERT INTO tb_missao (pontos_missao, titulo, tipo_missao_id, descricao, meta_missao, periodicidade_dias, prazo_dias, imagem_missao_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"missao_id"});
            ps.setInt(1, pontos);
            ps.setString(2, nome);
            ps.setLong(3, tipo.getId());
            ps.setString(4, descricao);
            ps.setDouble(5, meta);
            ps.setInt(6, periodicidade);
            ps.setInt(7, prazo);
            ps.setLong(8, imagemId);
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();


    }

    public List<Missao> buscarTodas(){
        String sql = """
                SELECT
                    m.missao_id, m.pontos_missao, m.titulo, m.descricao,
                    m.meta_missao, m.periodicidade_dias, m.prazo_dias, m.imagem_missao_id,
                    t.tipo_missao_id, t.nome AS tipo_nome, t.descricao AS tipo_descricao, t.unidade_medida AS tipo_unidade_medida
                FROM tb_missao m
                INNER JOIN tb_tipo_missao t ON m.tipo_missao_id = t.tipo_missao_id
                """;
        try{
            return jdbcTemplate.query(sql, missaoMapper);

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }


    public Missao buscar(long id) {
        String sql = """
                SELECT
                    m.missao_id, m.pontos_missao, m.titulo, m.descricao,
                    m.meta_missao, m.periodicidade_dias, m.prazo_dias, m.imagem_missao_id,
                    t.tipo_missao_id, t.nome AS tipo_nome, t.descricao AS tipo_descricao, t.unidade_medida AS tipo_unidade_medida
                FROM tb_missao m
                INNER JOIN tb_tipo_missao t ON m.tipo_missao_id = t.tipo_missao_id
                WHERE m.missao_id = ?
                """;
            return jdbcTemplate.queryForObject(sql, missaoMapper, id);

    }

    public int excluir(long id){
        String sql = "DELETE FROM tb_missao WHERE missao_id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public int editar(long id, int pontos, String titulo, String descricao, TipoMissao  tipo) {
        String sql = "UPDATE tb_missao SET pontos_missao = ?, titulo = ?, descricao = ?, tipo_missao = ? WHERE missao_id = ?";
        return jdbcTemplate.update(sql, pontos, titulo, descricao, tipo.getId(), id);
    }


    public List<Missao> buscarConcluidas(long usuarioId){
        String sql = """
                SELECT
                    m.missao_id, m.pontos_missao, m.titulo, m.descricao,
                    m.meta_missao, m.periodicidade_dias, m.prazo_dias, m.imagem_missao_id,
                    t.tipo_missao_id, t.nome AS tipo_nome, t.descricao AS tipo_descricao, t.unidade_medida AS tipo_unidade_medida
                FROM tb_missao m
                INNER JOIN tb_tipo_missao t ON m.tipo_missao_id = t.tipo_missao_id
                INNER JOIN tb_usuario_missao um ON um.missao_id = m.missao_id AND um.usuario_id = ?
                """;
        return jdbcTemplate.query(sql, missaoMapper, usuarioId);
    }

    public void completar(UsuarioSoulMove usuario, Missao missao){
        String sql = "INSERT INTO tb_usuario_missao (status_missao, pontuacao_recebida, usuario_id, missao_id) VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(sql, StatusMissao.CONCLUIDA.getStatus(), missao.getPontos(), usuario.getId(), missao.getId());
    }

    public int excluirDependencias(long id){
        String sql = "DELETE FROM tb_usuario_missao WHERE missao_id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public Map<String, Object> buscarImagem(long imagemId) {
        String sql = "SELECT dados_imagem, tipo_mime FROM tb_imagem_missao WHERE imagem_missao_id = ?";
        List<Map<String, Object>> resultados = jdbcTemplate.queryForList(sql, imagemId);
        if (resultados.isEmpty()) {
            throw new UnableToFindEntityException("Imagem não incontrada. ID: " + imagemId);
        }

        return resultados.get(0);
    }

    public long cadastrarImagem(byte[] dadosImagem, String tipoMime) {
        String sql = "INSERT INTO tb_imagem_missao (dados_imagem, tipo_mime) VALUES(?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update( con ->{
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"imagem_missao_id"});
            ps.setBytes(1, dadosImagem);
            ps.setString(2, tipoMime);
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();


    }
}
