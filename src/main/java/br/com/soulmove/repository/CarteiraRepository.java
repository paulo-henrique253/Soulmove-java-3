package br.com.soulmove.repository;

import br.com.soulmove.model.CarteiraUsuario;
import br.com.soulmove.model.UsuarioSoulMove;
import br.com.soulmove.model.exceptions.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CarteiraRepository {
    private final JdbcTemplate jdbcTemplate;

    public CarteiraRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public final RowMapper<CarteiraUsuario> carteiraMapper = (rs, rowNum) ->{
        long id = rs.getLong("carteira_id");
        double saldo = rs.getDouble("saldo");
        return new CarteiraUsuario(id, saldo);
    };

    public CarteiraUsuario cadastrar(UsuarioSoulMove usuario){
        String sql = "INSERT INTO tb_carteira (usuario_id, saldo_mobilidade) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"carteira_id"});
            ps.setLong(1, usuario.getId());
            ps.setDouble(2, 0.0);
            return ps;
        }, keyHolder);

        long id = keyHolder.getKey().longValue();
        return new CarteiraUsuario(id, 0.0);
    }

    public CarteiraUsuario buscar(long usuarioId){
        String sql = "SELECT carteira_id, saldo_mobilidade FROM tb_carteira WHERE usuario_id = ?";
        return jdbcTemplate.queryForObject(sql, carteiraMapper, usuarioId);
    }

    public void alterarSaldo(CarteiraUsuario carteira, double saldo){
        String sql = "UPDATE tb_carteira SET saldo_mobilidade = ? WHERE carteira_id = ?";
        jdbcTemplate.update(sql, OracleErrorParser.adjustPrecision(saldo, 2), carteira.getId());
    }
}
