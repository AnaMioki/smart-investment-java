package school.sptech;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CriarExcel {

    private  String fileName = "../ListaAcao.xlsx";

    public void criarExcel(List<Acao> lista) {
        ConexaoBanco con = new ConexaoBanco();
        GuardaLog log = new GuardaLog(con.getJdbcTemplate());

        LocalDateTime dataAtual = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheetAcoes = workbook.createSheet("Ações");

        int rownum = 0;
        for (Acao acao : lista) {
            Row row = sheetAcoes.createRow(rownum++);
            int cellnum = 0;

            Cell cellNome = row.createCell(cellnum++);
            cellNome.setCellValue(acao.getNome());
        }

        try (FileOutputStream out = new FileOutputStream(new File(this.fileName))) {
            workbook.write(out);
            workbook.close();
            System.out.println("Arquivo Excel criado com sucesso!");
            log.gardaLog("Sucesso", dataAtual.format(formatter), "Sucesso ao criar o arquivo!");

            fazerTratamento(this.fileName);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro ao criar/editar o arquivo!");
            log.gardaLog("Erro" , dataAtual.format(formatter), "Erro ao fazer a criação do arquivo! \n" + e.getMessage());
        }
    }

    public void fazerTratamento(String nomeArquivo){
        TratarExcel exec = new TratarExcel();
        exec.tratamentoDados(nomeArquivo);
        return;
    }


}
