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
        LocalDateTime dataAtual = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss:ss");
        dataAtual.format(formatter);


        System.out.println("\n\n Enviando dados ao banco:");

        System.out.println(list);



        for(int i = 0; i< list.size() ; i++ ) {
            String ticker = list.get(i).getTicker();
            String data = list.get(i).getData();
            Double abertura = list.get(i).getAbertura();
            Double fechamento = list.get(i).getFechamento();
            Double alta = list.get(i).getAlta();
            Double baixa = list.get(i).getBaixa();
            Double volume = list.get(i).getVolume();


            try {
                jdbcTemplate.update("INSERT INTO acoes (dtAtual, precoAbertura, precoFechamento, precoMaisAlto, precoMaisBaixo, volume, fkEmpresa) " + "VALUES (?, ?, ?, ?, ?, ?,  (SELECT idEmpresa FROM empresa WHERE ticker = ?))", data, abertura, fechamento, alta, baixa, volume, ticker);

            } catch (Exception e) {
                System.err.println(dataAtual + " - Erro ao realizar operação no banco!" );
                System.err.println("Ação: " + list.get(i).getTicker());
                System.err.println("Processo número: " + i);
                System.err.println("Mensagem: " + e.getMessage());

                return;
            }

            System.out.println(dataAtual + " -  Operação concluída! Ação: " + list.get(i).getTicker());
        }
        return;
    }

}
