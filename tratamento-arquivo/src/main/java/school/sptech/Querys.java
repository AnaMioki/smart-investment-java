package school.sptech;

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
        System.out.println(Date.parse(list.get(1).data));

        for(int i = 0; i< 10 ; i++ ) {

            try {
                jdbcTemplate.update("INSERT INTO acoes (dtAtual,precoAbertura,precoFechamento,precoMaisAlto,precoMaisBaixo,volume,fkEmpresa) " +
                        "VALUES (?,?,?,?,?,?,?)", Date.parse(list.get(i).data), list.get(i).Abertura, list.get(i).Fechamento, list.get(i).Alta,list.get(i).Baixa,list.get(i).volume,1);

            } catch (Exception e) {
                System.err.println(dataAtual + " - Erro ao realizar operação no banco!" );
                System.err.println("Ação: " + list.get(i).nome);
                System.err.println("Processo número: " + i);
                System.err.println("Mensagem: " + e.getMessage());

                return;
            }

            System.out.println(dataAtual + " -  Operação concluída! Ação: " + list.get(i).nome);
        }
        return;
    }

}
