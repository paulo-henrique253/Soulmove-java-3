package br.com.soulmove.repository;

import br.com.soulmove.model.Usuario;
import br.com.soulmove.model.UsuarioSoulMove;
import br.com.soulmove.model.Viagem;
import br.com.soulmove.model.exceptions.*;
import br.com.soulmove.model.type.Veiculo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class ViagemRepository {
    private final JdbcTemplate jdbcTemplate;

    public ViagemRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Viagem> viagemMapper = (rs, rowNum) ->{
        Viagem v = new Viagem();
        v.setId(rs.getLong("viagem_id"));
        v.setOrigem(rs.getString("origem"));
        v.setDestino(rs.getString("destino"));
        v.setTipoVeiculo(Veiculo.getTipoVeiculo(rs.getString("tipo_veiculo")));
        v.setKmPercorrido(rs.getDouble("km_percorrido"));
        v.setCarbonoEconomizado(rs.getDouble("carbono_economizado"));
        v.setCarbonoEmitido(rs.getDouble("carbono_emitido"));
        v.setData(rs.getDate("data_viagem").toLocalDate());

        //usuario
        long usuarioId = rs.getLong("usuario_id");
        String usuarioNome = rs.getString("usuario_nome");
        String usuarioEmail = rs.getString("usuario_email");
        LocalDate usuarioDataCadastro = rs.getDate("usuario_data_cadastro").toLocalDate();
        String usuarioSenha = rs.getString("usuario_senha");
        int usuarioPontos = rs.getInt("usuario_pontos");
        UsuarioSoulMove u = new UsuarioSoulMove(usuarioId, usuarioNome, usuarioPontos , usuarioEmail, usuarioDataCadastro, usuarioSenha);

        v.setUsuario(u);

        return v;
    };

    public Viagem registrar(String origem, String destino, Veiculo tipoVeiculo, double kmPercorrido, double carbonoEconomizado, double carbonoEmitido, UsuarioSoulMove usuario){
        String sql = "INSERT INTO tb_viagem (origem, destino, tipo_veiculo, km_percorrido, carbono_economizado, carbono_emitido, usuario_id) VALUES(?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con ->{
            PreparedStatement ps = con.prepareStatement(sql, new String[] {"VIAGEM_ID", "DATA_VIAGEM"});
            ps.setString(1, origem);
            ps.setString(2, destino);
            ps.setString(3,tipoVeiculo.getVeiculo());
            ps.setDouble(4, OracleErrorParser.adjustPrecision(kmPercorrido, 2));
            ps.setDouble(5, OracleErrorParser.adjustPrecision(carbonoEconomizado, 2));
            ps.setDouble(6, OracleErrorParser.adjustPrecision(carbonoEmitido, 2));
            ps.setLong(7, usuario.getId());
            return ps;
        });

        Map<String, Object> chaves = keyHolder.getKeys();
        if (chaves != null && !chaves.isEmpty()){
            Viagem v = new Viagem();
            long id = ((Number) chaves.get("VIAGEM_ID")).longValue();
            v.setId(id);
            v.setOrigem(origem);
            v.setDestino(destino);
            v.setTipoVeiculo(tipoVeiculo);
            v.setKmPercorrido(kmPercorrido);
            v.setCarbonoEconomizado(carbonoEconomizado);
            v.setCarbonoEmitido(carbonoEconomizado);
            LocalDate data = ((Date) chaves.get("DATA_VIAGEM")).toLocalDate();
            v.setData(data);
            v.setUsuario(usuario);
            return v;
        }
        throw new RuntimeException("Impossivel gerar as chaves de Viagem");
    }

    public List<Viagem> buscarHistorico(long usuarioId){
        String sql = """
            SELECT 
            v.viagem_id, v.data_viagem, v.origem, v.destino, v.tipo_veiculo, v.km_percorrido, v.carbono_economizado, v.carbono_emitido 
            u.usuaroi_id, u.nome AS usuario_nome, u.pontos AS usuario_pontos, u.email AS usuario_email, u.data_cadastro AS usuario_data_cadastro, u.senha AS usuario_senha
            FROM tb_viagem v
            INNER JOIN tb_usuario u ON v.usuario_id = u.usuario_id
            WHERE v.usuario_id = ?
        """;
        return jdbcTemplate.query(sql, viagemMapper, usuarioId);
    }

    public Viagem buscar(long id){
        String sql = """
            SELECT 
            v.viagem_id, v.data_viagem, v.origem, v.destino, v.tipo_veiculo, v.km_percorrido, v.carbono_economizado, v.carbono_emitido 
            u.usuaroi_id, u.nome AS usuario_nome, u.pontos AS usuario_pontos, u.email AS usuario_email, u.data_cadastro AS usuario_data_cadastro, u.senha AS usuario_senha
            FROM tb_viagem v
            INNER JOIN tb_usuario u ON v.usuario_id = u.usuario_id
            WHERE v.viagem_id = ?
        """;
        return jdbcTemplate.queryForObject(sql, viagemMapper, id);
    }

}
