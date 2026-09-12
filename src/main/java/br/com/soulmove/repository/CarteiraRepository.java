package br.com.soulmove.repository;

import br.com.soulmove.model.CarteiraUsuario;
import br.com.soulmove.model.UsuarioSoulMove;
import br.com.soulmove.model.exceptions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CarteiraRepository {

    public CarteiraUsuario cadastrar(UsuarioSoulMove usuario)
            throws ConstraintViolationException, NullDataException, DatabaseException, TooLargeException {
        String sql = "INSERT INTO tb_carteira (usuario_id, saldo_mobilidade) VALUES (?, ?)";
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql, new String[] {"CARTEIRA_ID"})){

            pstmt.setLong(1, usuario.getId());
            pstmt.setDouble(2, 0.0);

            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            long id = 0;

            if (rs.next()){
                id = rs.getBigDecimal(1).longValue();
            }

            return new CarteiraUsuario(id, usuario, 0.0);

        }catch (SQLException e){
            OracleExceptionTranslator.translateException(e, "Erro ao cadastrar carteira");
            return null;
        }
    }

    public CarteiraUsuario buscar(UsuarioSoulMove usuario)
            throws ConstraintViolationException, NullDataException, DatabaseException, TooLargeException, UnableToFindEntityException {
        String sql = "SELECT carteira_id, saldo_mobilidade FROM tb_carteira WHERE usuario_id = ?";
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)){
            pstmt.setLong(1, usuario.getId());

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()){
                long id = rs.getBigDecimal("carteira_id").longValue();
                double saldo = rs.getDouble("saldo_mobilidade");
                return new CarteiraUsuario(id, usuario, saldo);
            } else throw new UnableToFindEntityException("Erro ao buscar carteira: entidade não encontrada", "TB_CARTEIRA");

        } catch (SQLException e){
            OracleExceptionTranslator.translateException(e, "Erro ao buscar carteira");
            return null;
        }
    }

    public void alterarSaldo(CarteiraUsuario carteira, double saldo)
            throws ConstraintViolationException, NullDataException, DatabaseException, TooLargeException, UnableToFindEntityException {
        String sql = "UPDATE tb_carteira SET saldo_mobilidade = ? WHERE carteira_id = ?";
        try (Connection con = new ConnectionFactory().getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)){

            pstmt.setDouble(1, saldo);
            pstmt.setLong(2, carteira.getId());

            int registros = pstmt.executeUpdate();
            if (registros == 0)
                throw new UnableToFindEntityException("Erro ao alterar saldo: entidade não encontrada", "TB_CARTEIRA");
        } catch (SQLException e){
            OracleExceptionTranslator.translateException(e, "Erro ao alterar saldo");
        }
    }
}
