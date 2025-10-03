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

public class LeArquivo {



    public List<Empresa> extrairEmpresa(String nomeArquivo){
        LocalDateTime dataAtual = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss:ss");
        dataAtual.format(formatter);
        List<Empresa> lista = new ArrayList<>();


        try(InputStream arquivo = new FileInputStream(nomeArquivo);
            Workbook workbook = new XSSFWorkbook(arquivo)){

            System.out.println("Iniciando a leitura do arquivo " + dataAtual.format(formatter));
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {


                Cell cellticker = row.getCell(0);
                Cell cellNome = row.getCell(1);
                Cell cellSetor = row.getCell(2);
                Cell cellUrl = row.getCell(3);

                if (cellNome != null /*&&*//* !cellNome.toString().equalsIgnoreCase("Empresa não encontrada")&& !cellNome.toString().trim().equalsIgnoreCase("")*/){
                    String nome = cellNome.toString().trim();
                    String setor = cellSetor.toString();
                    String url = cellUrl.toString();
                    String ticker = cellticker.toString();
                    Empresa empresa = new Empresa(nome , setor, url, ticker);
                    lista.add(empresa);

                }
                System.out.println("\n\nLendo linha " + row.getRowNum() + " " + dataAtual.format(formatter));
            }

            conexaoBanco(lista);
            return lista;


        }catch(Exception e){
            System.err.println("Erro ao fazer a leitura dos arquivos " + dataAtual.format(formatter));
            System.err.println("Mensagem: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }


    private void conexaoBanco(List<Empresa> list) {
        ConexaoBanco con = new ConexaoBanco();
        Query exec = new Query(con.getJdbcTemplate());
        exec.insereEmpresa(list);
        return;
    }
}
