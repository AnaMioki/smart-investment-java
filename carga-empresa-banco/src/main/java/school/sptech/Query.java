package school.sptech;

import org.springframework.jdbc.core.JdbcTemplate;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Query {

    private final JdbcTemplate jdbcTemplate;

    public Query(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insereEmpresa(List<Empresa> list, String nomeArquivo) {
        LocalDateTime dataAtual = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss");
        String dataFormatada = dataAtual.format(formatter);


        System.out.println("\n\nEnviando dados ao banco...");

        jdbcTemplate.update("SET foreign_key_checks = 0");
        jdbcTemplate.update("TRUNCATE TABLE empresa");

        try {
            for (int i = 0; i < list.size(); i++) {
                if (i % 200 == 0) {
                    System.out.println("Enviando empresas: " + i);
                }

                Empresa emp = list.get(i);
                String nome = emp.getNome();
                String setor = emp.getSetor();
                String logo = emp.getImagem();
                String ticker = emp.getTicker();

                jdbcTemplate.update("INSERT INTO empresa (nome, setor, logo, ticker) VALUES (?, ?, ?, ?)",
                        nome, setor, logo, ticker);

                System.out.println(dataFormatada + " - Operação concluída! Ação: " + nome);
            }

            File arquivo = new File(nomeArquivo);
            if (arquivo.exists()) {
                if (arquivo.delete()) {
                    System.out.println("Arquivo local deletado com sucesso: " + nomeArquivo);
                } else {
                    System.err.println("Falha ao deletar o arquivo local: " + nomeArquivo);
                }
            } else {
                System.out.println("Arquivo já não existe localmente: " + nomeArquivo);
            }

        } catch (Exception e) {
            System.err.println(dataFormatada + " - Erro ao realizar operação no banco!");
            System.err.println("Mensagem: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
