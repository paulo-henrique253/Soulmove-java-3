package br.com.soulmove.repository;

import br.com.soulmove.model.Recarga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;

@Repository
public class RecargaRepository {

    private final JdbcTemplate jdbcTemplate;

    private RowMapper<Recarga> recargaMapper = (rs, rowNum) ->{
        return new Recarga();
    };

    @Autowired
    public RecargaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Recarga cadastrar(long usuarioId, String codigoPix, BigDecimal valorRecarga) {
        String sql = "INSERT INTO tb_recarga (usuario_id, codigo_pix_copia_cola, valor_recarga, status_recarga) VALUES(?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con ->{
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"recarga_id"});
            ps.setLong(1, usuarioId);
            ps.setString(2, codigoPix);
            ps.setDouble(3, OracleErrorParser.adjustPrecision(valorRecarga.doubleValue() + 10.0, 2));
            ps.setString(4, "pendente");
            return ps;
        }, keyHolder);

        return new Recarga();
    }

}
