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

    public void insereNome(List<Acao> list) {
        GuardaLog log = new GuardaLog(jdbcTemplate);

        LocalDateTime dataAtual = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        dataAtual.format(formatter);


        System.out.println("\n\n Enviando dados ao banco:");


        jdbcTemplate.update("SET foreign_key_checks = 0");
        jdbcTemplate.update("TRUNCATE TABLE acoes");
        log.gardaLog("Alerta", dataAtual.format(formatter), "Iniciando envio para o banco de dados!");
        List<Acao> acoesPerformaticas = new ArrayList<>();
        List<Integer> fksPerformaticas = new ArrayList<>();
        Integer i = 0;


        List<Empresa> listFKS = jdbcTemplate.query("SELECT idEmpresa as id, ticker FROM empresa;", new BeanPropertyRowMapper<>(Empresa.class));

        HashMap<String, Integer> mapeamentoDeIds = new HashMap<>();

        for (Empresa listFK : listFKS) {
            mapeamentoDeIds.put(listFK.getTicker(), listFK.getId());
        }

        for (Acao acao : list) {
            acoesPerformaticas.add(acao);
            i++;
            String ticker = acao.getTicker();

            try {
                    if (i % 50000 == 0) {
                        carregarLote(acoesPerformaticas, mapeamentoDeIds);
                        acoesPerformaticas.clear();
                        fksPerformaticas.clear();
                        System.out.println("Carregando ações..." + i);
                    }
                    // jdbcTemplate.update("INSERT INTO acoes (dtAtual, precoAbertura, precoFechamento, precoMaisAlto,precoMaisBaixo, volume, fkEmpresa) " + "VALUES (?, ?, ?,?, ?, ?, ?)", data, abertura, fechamento, alta, baixa, volume, fk);
            } catch (Exception e) {
                System.err.println(dataAtual + " - Erro ao realizar operação no banco!");
                System.err.println("Ação: " + acao.getTicker());
                System.err.println("Processo número: " + i);
                System.err.println("Mensagem: " + e.getMessage());
                log.gardaLog("Erro", dataAtual.format(formatter), "Erro ao guardar a ação: " + list.get(i).getTicker() + "\n " + e.getMessage());
            }
        }
        carregarLote(acoesPerformaticas, mapeamentoDeIds);
        System.out.println("Carregando ações..." + i);
        log.gardaLog("Sucesso", dataAtual.format(formatter), "Sucesso ao guardar ações no banco de dados!");
        System.out.println("Sucesso ao guardar as ações no banco de dados!");
        return;
    }

    public void carregarLote(List<Acao> acoes, Map<String, Integer> fks) {
        String sql = "INSERT INTO acoes " +
                "(dtAtual, precoAbertura, precoFechamento, precoMaisAlto, precoMaisBaixo, volume, fkEmpresa) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (var conexao = con.getBasicDataSource().getConnection();
             var ps = conexao.prepareStatement(sql)) {

            // 🔹 Desativa autocommit apenas nesta transação
            conexao.setAutoCommit(false);

            for (int i = 0; i < acoes.size(); i++) {
                Acao a = acoes.get(i);
                ps.setObject(1, a.getData());
                ps.setDouble(2, a.getAbertura());
                ps.setDouble(3, a.getFechamento());
                ps.setDouble(4, a.getAlta());
                ps.setDouble(5, a.getBaixa());
                ps.setDouble(6, a.getVolume());
                ps.setInt(7, fks.get(a.getTicker()));
                ps.addBatch();
            }

            ps.executeBatch();
            conexao.commit(); // 🔹 Confirma a transação

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("❌ Erro ao inserir lote: " + e.getMessage());
        }
    }
}