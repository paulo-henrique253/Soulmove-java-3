package br.com.soulmove.repository;

import br.com.soulmove.exceptions.BussinesRuleException;
import br.com.soulmove.model.CarteiraUsuario;
import br.com.soulmove.model.Conquista;
import br.com.soulmove.model.UsuarioHistorico;
import br.com.soulmove.model.UsuarioSoulMove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.Map;

@Repository
public class UsuarioRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UsuarioRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    RowMapper<UsuarioSoulMove> usuarioMapper = (rs, rowNum) ->{
        long usuarioId = rs.getLong("usuario_id");
        String usuarioNome = rs.getString("nome");
        String usuarioEmail = rs.getString("email");
        LocalDate usuarioDataCadastro = rs.getDate("data_cadastro").toLocalDate();
        String usuarioSenha = rs.getString("senha");
        int usuarioPontos = rs.getInt("pontos");

        UsuarioSoulMove u = new UsuarioSoulMove(usuarioId, usuarioNome, usuarioPontos , usuarioEmail, usuarioDataCadastro, usuarioSenha);


        //carteira
        long carteiraId = rs.getLong("carteira_id");
        double saldo = rs.getDouble("saldo_mobilidade");
        CarteiraUsuario ca = new CarteiraUsuario(carteiraId, saldo);

        u.setCarteira(ca);

        //conquista
        Long conquistaId = rs.getObject("conquista_id", Long.class);
        if (conquistaId != null){
            Conquista c = new Conquista();
            c.setId(conquistaId);
            c.setNome(rs.getString("conquista_nome"));
            c.setTitulo(rs.getString("conquista_titulo"));
            c.setDescricao(rs.getString("conquista_descricao"));
            c.setPontos(rs.getInt("conquista_pontos"));
            c.setDataConclusao(rs.getDate("conquista_data").toLocalDate());

            u.setTituloAtual(c);
        } else u.setTituloAtual(null);

        return u;
    };

    RowMapper<UsuarioHistorico> historicoMapper = (rs, rowNum) ->{
        UsuarioHistorico h = new UsuarioHistorico();
        h.setKgC02(rs.getDouble("kg_CO2"));
        h.setMissoesConcluidas(rs.getInt("missoes"));
        h.setTrajetosLongos(rs.getInt("trajetos"));

        return h;
    };


    public UsuarioSoulMove cadastrar(String nome, String email, String senha){
        String sql = "INSERT INTO TB_USUARIO (nome, email, senha) VALUES(?, ?, ?)";
        LocalDate data = LocalDate.now();
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[] { "USUARIO_ID"});
            ps.setString(1, nome);
            ps.setString(2, email);
            ps.setString(3, senha);

            return ps;
        }, keyHolder);

        long id = keyHolder.getKey().longValue();

        UsuarioSoulMove u = new UsuarioSoulMove(id, nome, 0, email,data, senha);
        return u;


    }

    public UsuarioSoulMove buscar(long id){
        String sql = """
                SELECT
                    u.usuario_id, u.nome, u.email, u.senha, u.data_cadastro, vw.pontos_totais AS pontos,
                    c.conquista_id, c.nome AS conquista_nome, c.titulo AS conquista_titulo, c.descricao AS conquista_descricao, c.pontos AS conquista_pontos,  uc.data_conquista AS conquista_data,
                    ca.carteira_id, ca.saldo_mobilidade
                FROM tb_usuario u
                LEFT JOIN tb_carteira ca ON ca.usuario_id = u.usuario_id
                LEFT JOIN tb_usuario_conquista uc
                    ON uc.usuario_id = u.usuario_id
                    AND uc.flag_titulo_atual = 'S'
                LEFT JOIN tb_conquista c ON c.conquista_id = uc.conquista_id
                LEFT JOIN vw_usuario_resumo vw ON vw.usuario_id = u.usuario_id
                WHERE u.usuario_id = ?
                """;
        return jdbcTemplate.queryForObject(sql, usuarioMapper, id);
    }

    public UsuarioSoulMove buscar(String email){
        String sql = """
                SELECT
                    u.usuario_id, u.nome, u.email, u.senha, u.data_cadastro, vw.pontos_totais AS pontos,
                    c.conquista_id, c.nome AS conquista_nome, c.titulo AS conquista_titulo, c.descricao AS conquista_descricao, c.pontos AS conquista_pontos,  uc.data_conquista AS conquista_data,
                    ca.carteira_id, ca.saldo_mobilidade
                FROM tb_usuario u
                LEFT JOIN tb_carteira ca ON ca.usuario_id = u.usuario_id
                LEFT JOIN tb_usuario_conquista uc
                    ON uc.usuario_id = u.usuario_id
                    AND uc.flag_titulo_atual = 'S'
                LEFT JOIN tb_conquista c ON c.conquista_id = uc.conquista_id
                LEFT JOIN vw_usuario_resumo vw ON vw.usuario_id = u.usuario_id
                WHERE u.email = ?
            """;
        return jdbcTemplate.queryForObject(sql, usuarioMapper, email);
    }

    public int alterarConquista(long usuarioId, long conquistaId){
        String sql = """
                UPDATE TB_USUARIO_CONQUISTA
                    SET flag_titulo_atual = 'S'
                    WHERE usuario_id = ?
                    AND conquista_id = ?
                """;
        return jdbcTemplate.update(sql, conquistaId, usuarioId);
    }

    public int excluir(UsuarioSoulMove usuario){
        String sql = "DELETE FROM tb_usuario WHERE id = ?";
        return jdbcTemplate.update(sql, usuario.getId());
    }


    public UsuarioHistorico buscarHistorico(long id) {
        String sql = "SELECT kg_CO2, trajetos, missoes FROM VW_USUARIO_HISTORICO WHERE usuario_id = ?";
        return jdbcTemplate.queryForObject(sql, historicoMapper , id);
    }

}
