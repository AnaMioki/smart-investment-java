package school.sptech;

import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Query{

    private final JdbcTemplate jdbcTemplate;

    public Query(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

    }

    public void insereEmpresa(List<Empresa> list){
        LocalDateTime dataAtual = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss:ss");
        dataAtual.format(formatter);


        System.out.println("\n\n Enviando dados ao banco:");

        System.out.println(list);


        jdbcTemplate.update("SET foreign_key_checks = 0");
        jdbcTemplate.update("TRUNCATE TABLE empresa");
        for(int i = 0; i< list.size() ; i++ ) {

            String nome = list.get(i).getNome();
            String setor = list.get(i).getSetor();
            String logo = list.get(i).getImagem();
            String ticker = list.get(i).getTicker();
            try {
                jdbcTemplate.update("INSERT INTO empresa (nome,setor,logo,ticker) " +
                        "VALUES (?,?,?,?)", nome , setor, logo, ticker);

            } catch (Exception e) {
                System.err.println(dataAtual + " - Erro ao realizar operação no banco!" );
                System.err.println("Empresa: " + list.get(i).getNome());
                System.err.println("Processo número: " + i);
                System.err.println("Mensagem: " + e.getMessage());

                return;
            }
            System.out.println(dataAtual + " -  Operação concluída! Ação: " + list.get(i).getNome());
        }
    }

}
