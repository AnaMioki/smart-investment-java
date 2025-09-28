package school.sptech;

import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Querys {

    private final JdbcTemplate jdbcTemplate;



    public Querys(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

    }

    public void insereNome(List<Acao> list){
        LocalDateTime dataAtual = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss:s");
        dataAtual.format(formatter);
        for(int i = 0; i< 10 ; i++ ) {
            try {
                jdbcTemplate.update("INSERT INTO musica VALUES (?)", list.get(i).nome);
            } catch (Exception e) {
                System.err.println(dataAtual + "Erro ao realizar operação no banco! ");
                return;
            }
            System.out.println(dataAtual + "Operação concluída!");
        }
        return;
    }

}
