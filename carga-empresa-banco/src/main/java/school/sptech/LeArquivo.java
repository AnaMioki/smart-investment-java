package school.sptech;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LeArquivo {



    public List<Empresa> extrairEmpresa(String nomeArquivo){
        ConexaoBanco con = new ConexaoBanco();
        GuardaLog log = new GuardaLog(con.getJdbcTemplate());
        LocalDateTime dataAtual = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
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

                if (cellNome != null && !cellNome.toString().equalsIgnoreCase("Empresa não encontrada")&& !cellNome.toString().trim().equalsIgnoreCase("")){
                    String nome = cellNome.toString().trim();
                    String setor = cellSetor.toString();
                    String url = cellUrl.toString();
                    String ticker = cellticker.toString();
                    Empresa empresa = new Empresa(nome , setor, url, ticker);
                    lista.add(empresa);

                }
                System.out.println("\n\nLendo linha " + row.getRowNum() + " " + dataAtual.format(formatter));

            }}catch(Exception e){

            log.gardaLog("Erro", dataAtual.format(formatter),"Erro ao fazer a leitura dos arquivos: " + e.getMessage());
            System.err.println("Erro ao fazer a leitura dos arquivos " + dataAtual.format(formatter));
            System.err.println("Mensagem: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Sucesso ao fazer a leitura dos arquivos!");
        log.gardaLog("Sucesso", dataAtual.format(formatter), "Sucesso ao fazer a leitura dos arquivos!");
        conexaoBanco(lista);
        return lista;
    }


    private void conexaoBanco(List<Empresa> list) {
        ConexaoBanco con = new ConexaoBanco();
        Query exec = new Query(con.getJdbcTemplate());
        exec.insereEmpresa(list);
        return;
    }
}
