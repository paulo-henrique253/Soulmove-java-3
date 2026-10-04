package br.com.soulmove.repository;


import br.com.soulmove.model.Missao;
import br.com.soulmove.model.UsuarioSoulMove;
import br.com.soulmove.model.exceptions.*;
import br.com.soulmove.model.type.StatusMissao;
import br.com.soulmove.model.type.TipoMissao;

import java.sql.PreparedStatement;
import java.util.List;

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
        m.setTipo(TipoMissao.getTipoMissao(rs.getString("tipo_missao")));
        return m;
    };

    public long cadastrar(int pontos, String nome, TipoMissao tipo, String descricao){
        String sql = "INSERT INTO tb_missao (pontos_missao, titulo, tipo_missao, descricao) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(sql, new String[]{"missao_id"});
                ps.setInt(1, pontos);
                ps.setString(2, nome);
                ps.setString(3, tipo.getTipo());
                ps.setString(4, descricao);
                return ps;
            }, keyHolder);
            return keyHolder.getKey().longValue();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }

    public List<Missao> buscarTodas(){
        String sql = "SELECT missao_id, pontos_missao, titulo, tipo_missao, descricao FROM tb_missao";
        try{
            return jdbcTemplate.query(sql, missaoMapper);

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }


    public Missao buscar(long id) {
        String sql = "SELECT pontos_missao, titulo, tipo_missao, descricao FROM tb_missao WHERE missao_id = ?";
            return jdbcTemplate.queryForObject(sql, missaoMapper, id);

    }

    public int excluir(long id){
        String sql = "DELETE FROM tb_missao WHERE missao_id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public int editar(long id, int pontos, String titulo, String descricao, TipoMissao tipo) {
        String sql = "UPDATE tb_missao SET pontos_missao = ?, titulo = ?, descricao = ?, tipo_missao = ? WHERE missao_id = ?";
        return jdbcTemplate.update(sql, pontos, titulo, descricao, tipo.getTipo(), id);
    }


    public List<Missao> buscarConcluidas(long usuarioId){
        String sql = "SELECT missao_id, pontos_missao, titulo, tipo_missao, descricao FROM tb_missao WHERE missao_id IN (SELECT missao_id FROM tb_usuario_missao WHERE usuario_id = ? and status_missao = 'concluida')";
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
}
