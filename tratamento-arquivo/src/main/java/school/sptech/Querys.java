package school.sptech;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Querys {

    private final JdbcTemplate jdbcTemplate;



    public Querys(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

    }

    public void insereNome(List<Acao> list){

        ConexaoBanco con = new ConexaoBanco();
        GuardaLog log = new GuardaLog(con.getJdbcTemplate());

        LocalDateTime dataAtual = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        dataAtual.format(formatter);


        System.out.println("\n\n Enviando dados ao banco:");


        jdbcTemplate.update("SET foreign_key_checks = 0");
        jdbcTemplate.update("TRUNCATE TABLE acoes");

        for(int i = 0; i< list.size() ; i++ ) {
            String ticker = list.get(i).getTicker();
            String data = list.get(i).getData();
            Double abertura = list.get(i).getAbertura();
            Double fechamento = list.get(i).getFechamento();
            Double alta = list.get(i).getAlta();
            Double baixa = list.get(i).getBaixa();
            Double volume = list.get(i).getVolume();

            try {
                Integer fk;
                try {
                    fk = jdbcTemplate.queryForObject("SELECT idEmpresa FROM empresa WHERE ticker = ?", Integer.class, ticker);
                }catch (Exception e){
                    fk = null;
                }
                if(fk!=null){
                    jdbcTemplate.update("INSERT INTO acoes (dtAtual, precoAbertura, precoFechamento, precoMaisAlto,precoMaisBaixo, volume, fkEmpresa) " + "VALUES (?, ?, ?,?, ?, ?, ?, ?)", data, abertura, fechamento, alta, baixa, volume, fk);
                }
            } catch (Exception e) {
                System.err.println(dataAtual + " - Erro ao realizar operação no banco!" );
                System.err.println("Ação: " + list.get(i).getTicker());
                System.err.println("Processo número: " + i);
                System.err.println("Mensagem: " + e.getMessage());
                log.gardaLog("Erro",  dataAtual.format(formatter), "Erro ao guardar a ação: " + list.get(i).getTicker() + "\n "+ e.getMessage());
            }
            if(i % 1000 == 0){
                System.out.println("Carregando ações... " + i);
            }
        }
        log.gardaLog("Sucesso" , dataAtual.format(formatter),"Sucesso ao guardar ações no banco de dados!");
        System.out.println("Sucesso ao guardar as ações no banco de dados!");
        return;
    }

}
