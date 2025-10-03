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
import java.util.List;

public class LeitorExcel {





    public List<Acao> extrairAcoes(String nomeArquivo) {
        LocalDateTime dataAtual = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss:ss");
        dataAtual.format(formatter);
        List<Acao> lista = new ArrayList<>();
        Integer contador = 0;

        try(
            InputStream arquivo = new FileInputStream(nomeArquivo);
            Workbook workbook = new XSSFWorkbook(arquivo)){

            System.out.println("Iniciando a leitura do arquivo " + dataAtual.format(formatter));Sheet sheet = workbook.getSheetAt(0);


            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // pular cabeçalho

                Cell cell = row.getCell(0); // sempre a primeira célula
                if (cell != null) {
                    String conteudo = cell.toString();
                    String[] partes = conteudo.split(",");


                        String data = partes[0].toString();
                        String ticker = partes[1].toString().trim().replace(" ", "");
                        Double abertura = Double.parseDouble(partes[2].replace(",", "."));
                        Double fechamento =Double.parseDouble(partes[3].replace(",", "."));
                        Double alta = Double.parseDouble(partes[4].replace(",", "."));
                        Double baixa =Double.parseDouble(partes[5].replace(",", "."));
                        Double volume = Double.parseDouble(partes[6].replace(",", "."));

                        Acao acao = new Acao(data, ticker, abertura, fechamento, alta, baixa, volume);
                        lista.add(acao);
                        contador++;

                }

                System.out.println("\n\nLendo linha " + row.getRowNum() + " " + dataAtual.format(formatter));

                if (contador > 10) {
                    conexaoBanco(lista);
                    return lista;
                }
            }
        }catch (Exception e){
            System.err.println("Erro ao fazer a leitura dos arquivos " + dataAtual.format(formatter));
            System.err.println("Mensagem: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;


    }


    private void conexaoBanco(List<Acao> list) {
        ConexaoBanco con = new ConexaoBanco();
        Querys exec = new Querys(con.getJdbcTemplate());
        exec.insereNome(list);

    }
}
