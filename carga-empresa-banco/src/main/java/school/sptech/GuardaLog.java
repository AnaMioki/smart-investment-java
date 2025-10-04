package school.sptech;

import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;

public class GuardaLog {

    private final JdbcTemplate jdbcTemplate;

    public GuardaLog(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

    }

    public void gardaLog(String tipo, String  horario, String mensagem){

            jdbcTemplate.update("INSERT INTO log VALUE (DEFAULT , ? , ? , ? )", tipo, horario, mensagem);
            return;
    }

}
