package br.com.soulmove.repository;

import br.com.soulmove.model.Usuario;
import br.com.soulmove.model.UsuarioSoulMove;
import br.com.soulmove.model.Viagem;
import br.com.soulmove.model.exceptions.*;
import br.com.soulmove.model.type.Veiculo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ViagemRepository {
    public Viagem registrar(String origem, String destino, Veiculo tipoVeiculo, double km_percorrido, double carbono_economizado, double carbono_emitido, UsuarioSoulMove usuario)
            throws DatabaseException, ConstraintViolationException, NullDataException, TooLargeException {
        String sql = "INSERT INTO tb_viagem (origem, destino, tipo_veiculo, km_percorrido, carbono_economizado, carbono_emitido, usuario_id) VALUES(?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql, new String[] {"VIAGEM_ID", "DATA_VIAGEM"})){
            pstmt.setString(1, origem);
            pstmt.setString(2, destino);
            pstmt.setString(3,tipoVeiculo.getVeiculo());
            pstmt.setDouble(4, OracleErrorParser.adjustPrecision(km_percorrido, 2));
            pstmt.setDouble(5, OracleErrorParser.adjustPrecision(carbono_economizado, 2));
            pstmt.setDouble(6, OracleErrorParser.adjustPrecision(carbono_emitido, 2));
            pstmt.setLong(7, usuario.getId());

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();

            if (rs.next()){
                Viagem viagem = new Viagem();

                viagem.setData(rs.getDate(2).toLocalDate());
                viagem.setId(rs.getBigDecimal(1).longValue());
                viagem.setOrigem(origem);
                viagem.setDestino(destino);
                viagem.setCarbonoEmitido(carbono_emitido);
                viagem.setKmPercorrido(km_percorrido);
                viagem.setUsuario(usuario);

                return viagem;
            }

        }catch (SQLException e){
            OracleExceptionTranslator.translateException(e, "Erro ao registrar viagem");
        }

        return null;

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
