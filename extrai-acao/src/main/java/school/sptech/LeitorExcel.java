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
                dataAtual = LocalDateTime.now();
                if (row.getRowNum() == 0) continue; // pular cabeçalho

                Cell cell = row.getCell(0); // sempre a primeira célula
                if (cell != null) {
                    String conteudo = cell.toString();
                    String[] partes = conteudo.split(",");

                    String nome = partes[1].toString();

                    if (!lista.contains(nome)){
                        Acao acao = new Acao(nome);
                        lista.add(acao);
                        contador++;
                    }

                }

                System.out.println("\n\nLendo linha " + row.getRowNum() + " " + dataAtual.format(formatter));

            }
        }catch (Exception e){
            System.err.println("Erro ao fazer a leitura dos arquivos " + dataAtual.format(formatter));
            System.err.println("Mensagem: " + e.getMessage());
            e.printStackTrace();
        }

        construirArquivo(lista);
        return lista;


    }

    public void construirArquivo(List<Acao> list){
        CriarExcel exec = new CriarExcel();
        exec.criarExcel(list);
        return;
    }



}
