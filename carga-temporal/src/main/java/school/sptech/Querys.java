package school.sptech;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Querys {

    private final JdbcTemplate jdbcTemplate;
    private ConexaoBanco con;

    public Querys(JdbcTemplate jdbcTemplate, ConexaoBanco con) {
        this.jdbcTemplate = jdbcTemplate;
        this.con = con;
    }

    public void insereNome(List<InfoTemporal> list) {
        GuardaLog log = new GuardaLog(jdbcTemplate);

        LocalDateTime dataAtual = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        dataAtual.format(formatter);


        System.out.println("\n\n Enviando dados ao banco:");


        jdbcTemplate.update("SET foreign_key_checks = 0");
        jdbcTemplate.update("TRUNCATE TABLE infoTemporal");
        log.gardaLog("Alerta", dataAtual.format(formatter), "Iniciando envio para o banco de dados!");
        List<InfoTemporal> infosPerformaticas = new ArrayList<>();
        List<Integer> fksPerformaticas = new ArrayList<>();
        Integer i = 0;


        List<Empresa> listFKS = jdbcTemplate.query("SELECT idEmpresa as id, ticker FROM empresa;", new BeanPropertyRowMapper<>(Empresa.class));

        HashMap<String, Integer> mapeamentoDeIds = new HashMap<>();

        for (Empresa listFK : listFKS) {
            mapeamentoDeIds.put(listFK.getTicker(), listFK.getId());
        }

        for (InfoTemporal info : list) {
            infosPerformaticas.add(info);
            i++;
            String ticker = info.getNome();

            try {
                    if (i % 500 == 0) {
                        carregarLote(infosPerformaticas, mapeamentoDeIds);
                        infosPerformaticas.clear();
                        fksPerformaticas.clear();
                        System.out.println("Carregando ações..." + i);
                    }
                    // jdbcTemplate.update("INSERT INTO acoes (dtAtual, precoAbertura, precoFechamento, precoMaisAlto,precoMaisBaixo, volume, fkEmpresa) " + "VALUES (?, ?, ?,?, ?, ?, ?)", data, abertura, fechamento, alta, baixa, volume, fk);
            } catch (Exception e) {
                System.err.println(dataAtual + " - Erro ao realizar operação no banco!");
                System.err.println("Ação: " + info.getNome());
                System.err.println("Processo número: " + i);
                System.err.println("Mensagem: " + e.getMessage());
                log.gardaLog("Erro", dataAtual.format(formatter), "Erro ao guardar a ação: " + list.get(i).getNome() + "\n " + e.getMessage());
            }
        }
        carregarLote(infosPerformaticas, mapeamentoDeIds);
        System.out.println("Carregando ações..." + i);
        log.gardaLog("Sucesso", dataAtual.format(formatter), "Sucesso ao guardar ações no banco de dados!");
        System.out.println("Sucesso ao guardar as ações no banco de dados!");
        return;
    }

    public void carregarLote(List<InfoTemporal> infos, Map<String, Integer> fks) {
        String sql = "INSERT INTO infoTemporal " +
                "(valorMercado, partrimonioLiquido, patrimonioLiquidoAcao, multiploSetorial, rentabilidadeAnual, infoTemporalcol, precoSobreValorPatrimonial, EBTDA, DRE, fkEmpresa, ano) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (var conexao = con.getBasicDataSource().getConnection();
             var ps = conexao.prepareStatement(sql)) {

            conexao.setAutoCommit(false);

            for (int i = 0; i < infos.size(); i++) {
                InfoTemporal a = infos.get(i);
                ps.setDouble(1,  a.getValorMercado());
                ps.setDouble(2,  a.getPartrimonioLiquido());
                ps.setDouble(3, a.getPatrimonioLiquidoAcao());
                ps.setInt(4,   a.getMultiploSetorial());
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