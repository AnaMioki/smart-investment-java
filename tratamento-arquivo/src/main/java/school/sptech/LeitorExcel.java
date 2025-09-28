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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss: ss");
        dataAtual.format(formatter);
        List<Acao> lista = new ArrayList<>();
        Integer contador = 0;

        try(
            InputStream arquivo = new FileInputStream(nomeArquivo);
            Workbook workbook = new XSSFWorkbook(arquivo)){

            System.out.println("Iniciando a leitura do arquivo " + dataAtual.format(formatter));
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                Cell cell = row.getCell(row.getRowNum());
                if (cell != null) {
                    String conteudo = cell.toString();
                    String[] partes = conteudo.split(",");
                    String data = partes[0];
                    String nome = partes[1];
                    Double abertura = Double.parseDouble(partes[2]);
                    Double fechamento = Double.parseDouble(partes[3]);
                    Double alta = Double.parseDouble(partes[4]);
                    Double baixa = Double.parseDouble(partes[5]);
                    Double volume = Double.parseDouble(partes[6]);


                    Acao acao = new Acao(data, nome, abertura, fechamento, alta, baixa, volume);
                    lista.add(acao);
                    contador++;
                }


                System.out.println("\n\n  Lendo linha " + row.getRowNum() + " " + dataAtual.format(formatter));


                if(contador > 10 ){
                    workbook.close();
                    conexaoBanco(lista);
                    return lista;
                }

            }
        }catch (Exception e){
            System.err.println("Erro ao fazer a leitura dos arquivos " + dataAtual.format(formatter));
        }

        return lista;


    }


    private void conexaoBanco(List<Acao> list) {
        ConexaoBanco con = new ConexaoBanco();
        Querys exec = new Querys(con.getJdbcTemplate());
        exec.insereNome(list);
    }
}
