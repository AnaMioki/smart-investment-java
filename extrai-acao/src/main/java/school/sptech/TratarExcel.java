package school.sptech;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TratarExcel {
    private String token = "dbESckEYvyLTKmNAE9HL3v";



    public void tratamentoDados(String nomeArquivo){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss");
        List<Empresa> lista = new ArrayList<>();

        try(InputStream arquivo = new FileInputStream(nomeArquivo);
            Workbook workbook = new XSSFWorkbook(arquivo)){

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if(row.getRowNum() == 0) continue;

                Cell cell = row.getCell(0);

                if(cell == null) continue;

                String conteudo = cell.toString();

                Resultado resultado = infos(token, conteudo);

                Empresa execEmpresa = new Empresa();

                Cell cellNome=   row.createCell(1);
                cellNome.setCellValue(resultado.getResults().get(0).getNome().replace(" ", "-"));



            }

        }catch(Exception e){
            System.err.println("Erro ao fazer a leitura do arquivo " + LocalDateTime.now().format(formatter));
            e.printStackTrace();
        }



    }

    public Resultado infos(String token, String ticker) {
        Resultado resultado = null;
        try {
            String url = "https://brapi.dev/api/quote/" + ticker
                    + "?token=" + token
                    + "&modules=summaryProfile";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            String json = response.body();

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            resultado = objectMapper.readValue(json, Resultado.class);

            System.out.println(resultado.getResults());

            System.out.println("Nome: " + resultado.getResults().get(0).getNome().replace(" ", "-"));


        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultado;
    }


}
