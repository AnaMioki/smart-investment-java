package school.sptech;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LeitorExcel {

    public List<Acao> extrairAcoes(String nomeArquivo) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dataAtual = LocalDateTime.now();

        ConexaoBanco con = new ConexaoBanco();
        GuardaLog log = new GuardaLog(con.getJdbcTemplate());

        System.out.println("Iniciando a leitura do arquivo " + dataAtual.format(formatter));

        List<Acao> lista = new ArrayList<>();
        Set<String> nomesUnicos = new HashSet<>();

        try (InputStream arquivo = new FileInputStream(nomeArquivo);
             Workbook workbook = new XSSFWorkbook(arquivo)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // cabeçalho

                Cell cell = row.getCell(0);
                if (cell == null) continue;

                String conteudo = cell.toString();
                String[] partes = conteudo.split(",");

                if (partes.length > 1) {
                    String nome = partes[1].trim().replace(" ", "");

                    if (nomesUnicos.add(nome)) {
                        lista.add(new Acao(nome));
                    }
                }

                if (row.getRowNum() % 1000 == 0) {
                    System.out.println("Lendo linha " + row.getRowNum() + " - " + LocalDateTime.now().format(formatter));
                }
            }

        } catch (Exception e) {
            System.err.println("Erro ao fazer a leitura do arquivo " + LocalDateTime.now().format(formatter));
            e.printStackTrace();
            log.gardaLog("Erro" , dataAtual.format(formatter), "Erro ao fazer a leitura do arquivo! \n" + e.getMessage());
        }

        System.out.println("Sucesso ao ler o arquivo!");
        log.gardaLog("Sucesso" , dataAtual.format(formatter), "Sucesso ao ler o arquivo!");
        construirArquivo(lista);
        return lista;
    }


    public void construirArquivo(List<Acao> list){
        CriarExcel exec = new CriarExcel();
        exec.criarExcel(list);
        return;
    }



}
