package school.sptech;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class CriarExcel {

    private  String fileName = "../ListaAcao.xls";

    public void criarExcel(List<Acao> lista) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheetAcoes = workbook.createSheet("Ações");

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
            fazerTratamento(this.fileName);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro ao criar/editar o arquivo!");
        }
    }

    public void fazerTratamento(String nomeArquivo){
        TratarExcel exec = new TratarExcel();
        exec.tratamentoDados(nomeArquivo);
        return;
    }


}
