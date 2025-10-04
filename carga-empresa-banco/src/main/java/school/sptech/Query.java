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
        ConexaoBanco con = new ConexaoBanco();
        GuardaLog log = new GuardaLog(con.getJdbcTemplate());
        LocalDateTime dataAtual = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
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


                log.gardaLog("Erro", dataAtual.format(formatter),"Erro ao fazer ao guardar as empresa: " + list.get(i).getNome()+ "\n"+  e.getMessage());

                return;
            }
            System.out.println(dataAtual + " -  Operação concluída! Ação: " + list.get(i).getNome());
        }

        log.gardaLog("Sucesso" , dataAtual.format(formatter), "Sucesso ao carregar as empresas no banco de dados!");
        System.out.println("Sucesso ao carregar as empresas no banco de dados!" + dataAtual.format(formatter));

    }

}
