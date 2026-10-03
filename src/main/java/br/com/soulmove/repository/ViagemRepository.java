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
        return v;
    }

    public Viagem registrar(String origem, String destino, Veiculo tipoVeiculo, double kmPercorrido, double carbonoEconomizado, double carbonoEmitido, long usuarioId){
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
            ps.setLong(7, usuarioId);
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
            return v;
        }
        throw new RuntimeException("Impossivel gerar as chaves de Viagem");
    }

    public List<Viagem> buscarHistorico(UsuarioSoulMove usuario)
        throws DatabaseException, ConstraintViolationException, NullDataException, TooLargeException, UnableToFindEntityException{
        String sql = "SELECT viagem_id, data_viagem, origem, destino, tipo_veiculo, km_percorrido, carbono_economizado, carbono_emitido, usuario_id FROM tb_viagem WHERE usuario_id = ?";
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)){
            pstmt.setLong(1, usuario.getId());

            ResultSet rs = pstmt.executeQuery();

            List<Viagem> viagens = new ArrayList<>();
            while (rs.next()){
                Viagem viagem = new Viagem();

                viagem.setData(rs.getDate("data_viagem").toLocalDate());
                viagem.setId(rs.getBigDecimal("viagem_id").longValue());
                viagem.setOrigem(rs.getString("origem"));
                viagem.setDestino(rs.getString("destino"));
                viagem.setCarbonoEmitido(rs.getDouble("carbono_emitido"));
                viagem.setKmPercorrido(rs.getDouble("km_percorrido"));
                viagem.setUsuario(new UsuarioRepository().buscar(rs.getBigDecimal("usuario_id").longValue()));
                viagem.setTipoVeiculo(Veiculo.getTipoVeiculo(rs.getString("tipo_veiculo")));
                viagem.setCarbonoEconomizado(rs.getDouble("carbono_economizado"));
                viagens.add(viagem);
            }
            return viagens;

        }catch (SQLException e){
            OracleExceptionTranslator.translateException(e, "Erro ao Obter histórico de viagem");
        }

        return null;
    }

    public Viagem buscar(long id)
            throws DatabaseException, ConstraintViolationException, NullDataException, TooLargeException, UnableToFindEntityException{
        String sql = "SELECT FROM tb_viagem viagem_id, data_viagem, origem, destino, tipo_veiculo, km_percorrido, carbono_economizado, carbono_emitido, usuario_id WHERE viagem_id = ?";
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)){
            pstmt.setLong(1, id);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()){

                Viagem viagem = new Viagem();
                viagem.setData(rs.getDate("data_viagem").toLocalDate());
                viagem.setId(rs.getBigDecimal("viagem_id").longValue());
                viagem.setOrigem(rs.getString("origem"));
                viagem.setDestino(rs.getString("destino"));
                viagem.setCarbonoEmitido(rs.getDouble("carbono_emitido"));
                viagem.setKmPercorrido(rs.getDouble("km_percorrido"));
                viagem.setUsuario(new UsuarioRepository().buscar(rs.getBigDecimal("usuario_id").longValue()));

                return viagem;
            }
            else{
                throw new UnableToFindEntityException("Erro ao buscar viagem: entidade não encontrada", "TB_VIAGEM");
            }
        }catch (SQLException e){
            OracleExceptionTranslator.translateException(e, "Erro ao registrar viagem");
        }

        return null;
    }

}
