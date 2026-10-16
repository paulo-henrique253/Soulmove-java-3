package br.com.soulmove.repository;

import br.com.soulmove.model.TipoMissao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TipoMissaoRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TipoMissaoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<TipoMissao> tipoMissaoMapper = (rs, numRo2) ->{
        TipoMissao tp = new TipoMissao();
        tp.setId(rs.getLong("tipo_missao_id"));
        tp.setNome(rs.getString("nome"));
        tp.setDescricao(rs.getString("descricao"));
        tp.setUnidadeMedida(rs.getString("unidade_medida"));
        return tp;
    };

    public TipoMissao buscar(String nome) {
        String sql = """
                SELECT tipo_missao_id, nome, descricao, unidade_medida
                FROM tb_tipo_missao
                WHERE nome = ?
                """;

        return jdbcTemplate.queryForObject(sql, tipoMissaoMapper, nome);

    }

    public List<TipoMissao> buscarTodos(){
        String sql = """
                SELECT tipo_missao_id, nome, descricao, unidade_medida
                FROM tb_tipo_missao
                """;
        return jdbcTemplate.query(sql, tipoMissaoMapper);
    }

    public boolean isMissao(String nome){
        String sql = "SELECT tipo_missao_id FROM tb_tipo_missao WHERE nome = ?";
        try{
            jdbcTemplate.query(sql, tipoMissaoMapper);
            return true;
        }catch (EmptyResultDataAccessException e){
            return false;
        }
    }
}
