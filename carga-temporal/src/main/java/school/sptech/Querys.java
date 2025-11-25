package school.sptech;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import school.sptech.classes.Empresa;
import school.sptech.controllers.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Querys {

    private final JdbcTemplate jdbcTemplate;
    private ConexaoBanco con;
    private ChamarSlack chamarSlack = new ChamarSlack();
    S3Controller controller = new S3Controller();

    public Querys(JdbcTemplate jdbcTemplate, ConexaoBanco con) {
        this.jdbcTemplate = jdbcTemplate;
        this.con = con;
    }

    public void insereNome(List<InfoTemporal> listBase2024) {

        GuardaLog log = new GuardaLog(jdbcTemplate);

        LocalDateTime dataAtual = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        System.out.println("\n\n Enviando dados ao banco:");

        jdbcTemplate.update("SET foreign_key_checks = 0");
        jdbcTemplate.update("TRUNCATE TABLE infoTemporal");

        log.gardaLog("Alerta", dataAtual.format(formatter), "Iniciando envio para o banco de dados!");

        List<InfoTemporal> listaExpandida = new ArrayList<>();

        List<Empresa> listFKS =
                jdbcTemplate.query("SELECT idEmpresa as id, ticker FROM empresa;",
                        new BeanPropertyRowMapper<>(Empresa.class));

        HashMap<String, Integer> mapeamentoDeIds = new HashMap<>();
        for (Empresa e : listFKS) {
            mapeamentoDeIds.put(e.getTicker(), e.getId());
        }

        for (InfoTemporal base2024 : listBase2024) {
            List<InfoTemporal> historico = GeradorHistorico.gerarSerieHistorica(base2024);
            listaExpandida.addAll(historico);
        }

        List<InfoTemporal> buffer = new ArrayList<>();
        int i = 0;

        for (InfoTemporal info : listaExpandida) {
            buffer.add(info);
            i++;

            if (i % 500 == 0) {
                carregarLote(buffer, mapeamentoDeIds);
                buffer.clear();
                System.out.println("Carregando ações..." + i);
            }
        }

        carregarLote(buffer, mapeamentoDeIds);

        log.gardaLog("Sucesso", dataAtual.format(formatter),
                "Sucesso ao guardar ações no banco de dados!");

        chamarSlack.tratarMensagem("---Arquivo de execução finalizado com sucesso!: carga-temporal ---");
    }


    public void carregarLote(List<InfoTemporal> infos, Map<String, Integer> fks) {
        String sql = "INSERT INTO infoTemporal " +
                "(valorMercado, patrimonioLiquido, patrimonioLiquidoAcao, multiploSetorial, rentabilidadeAnual, infoTemporalcol, precoSobreValorPatrimonial, EBITDA, DRE, fkEmpresa, ano) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (var conexao = con.getBasicDataSource().getConnection();
             var ps = conexao.prepareStatement(sql)) {

            conexao.setAutoCommit(false);

            for (int i = 0; i < infos.size(); i++) {
                InfoTemporal a = infos.get(i);
                ps.setDouble(1, a.getValorMercado());
                ps.setDouble(2, a.getPartrimonioLiquido());
                ps.setDouble(3, a.getPatrimonioLiquidoAcao());
                ps.setInt(4, a.getMultiploSetorial());
                ps.setDouble(5, a.getRentabilidadeAnual());
                ps.setDouble(6, a.getInfoTemporalcol());
                ps.setDouble(7, a.getPrecoSobreValorPatrimonial());
                ps.setDouble(8, a.getEBTDA());
                ps.setDouble(9, a.getDRE());
                ps.setInt(10, fks.get(a.getNome()));
                ps.setInt(11, a.getAno());
                ps.addBatch();
            }

            ps.executeBatch();
            conexao.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erro ao inserir lote: " + e.getMessage());
        }
    }

}

