package school.sptech;

import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Querys {

    private final JdbcTemplate jdbcTemplate;

    public Querys(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

    }

    public void insereNome(String nome){
        LocalDateTime dataAtual = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss:SS");
        dataAtual.format(formatter);


        try{

            jdbcTemplate.update("INSERT INTO musica VALUES (?)" ,nome);

        }catch (Exception e){
            System.err.println(dataAtual + "Erro ao realizar operação no banco! ");
            return;
        }
        System.out.println(dataAtual + "Operação concluída!");
        return;
    }

}
