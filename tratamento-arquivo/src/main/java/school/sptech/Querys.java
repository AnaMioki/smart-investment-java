package school.sptech;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Querys {

    private final JdbcTemplate jdbcTemplate;


    public Querys(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

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
        for (Acao acao : list) {
            acoesPerformaticas.add(acao);
            i++;
            String ticker = acao.getTicker();

            try {
                Integer fk = 1;
                fksPerformaticas.add(1);
//                try {
//                    fk = jdbcTemplate.queryForObject("SELECT idEmpresa FROM empresa WHERE ticker = ?", Integer.class, ticker);
//                    fksPerformaticas.add(fk);
//                } catch (Exception e) {
//                    fk = null;
//                    System.err.println("Erro ao encontrar empresa");
//                    e.getMessage();
//                }
                if (fk != null) {
                    if (i % 10000 == 0) {
                        carregarLote(acoesPerformaticas, fksPerformaticas);
                        acoesPerformaticas.clear();
                        fksPerformaticas.clear();
                        System.out.println("Carregando ações..." + i);
                    }
                    // jdbcTemplate.update("INSERT INTO acoes (dtAtual, precoAbertura, precoFechamento, precoMaisAlto,precoMaisBaixo, volume, fkEmpresa) " + "VALUES (?, ?, ?,?, ?, ?, ?)", data, abertura, fechamento, alta, baixa, volume, fk);
                }
            } catch (Exception e) {
                System.err.println(dataAtual + " - Erro ao realizar operação no banco!");
                System.err.println("Ação: " + acao.getTicker());
                System.err.println("Processo número: " + i);
                System.err.println("Mensagem: " + e.getMessage());
                log.gardaLog("Erro", dataAtual.format(formatter), "Erro ao guardar a ação: " + list.get(i).getTicker() + "\n " + e.getMessage());
            }
        }
        carregarLote(acoesPerformaticas, fksPerformaticas);
        log.gardaLog("Sucesso", dataAtual.format(formatter), "Sucesso ao guardar ações no banco de dados!");
        System.out.println("Sucesso ao guardar as ações no banco de dados!");
        return;
    }

    public void carregarLote(List<Acao> acoes, List<Integer> fks) {
        List<Object[]> tratada = new ArrayList<>();
        String sql = "INSERT INTO acoes (dtAtual, precoAbertura, precoFechamento, precoMaisAlto,precoMaisBaixo, volume, fkEmpresa) " + "VALUES (?, ?, ?,?, ?, ?, ?)";
        for (int i = 0; i < acoes.size(); i++) {
            Object[] valores = new Object[]{
                    acoes.get(i).getData(),
                    acoes.get(i).getAbertura(),
                    acoes.get(i).getFechamento(),
                    acoes.get(i).getAlta(),
                    acoes.get(i).getBaixa(),
                    acoes.get(i).getVolume(),
                    fks.get(i)
            };
            tratada.add(valores);
        }

        jdbcTemplate.batchUpdate(sql, tratada);
    }

}