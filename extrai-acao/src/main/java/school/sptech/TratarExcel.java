package school.sptech;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
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

                Empresa empresa = resultado.getResults().get(0);

                Cell cellNome = row.createCell(1);
                Cell cellSetor = row.createCell(2);
                Cell cellUrl = row.createCell(3);

                cellNome.setCellValue(empresa.getNome());

                cellSetor.setCellValue(
                        (empresa.getSummaryProfile() != null && empresa.getSummaryProfile().getSector() != null)
                                ? empresa.getSummaryProfile().getSector()
                                : "Sem setor"
                );

                cellUrl.setCellValue(
                        empresa.getUrlImagem() != null ? empresa.getUrlImagem() : "Sem imagem"
                );



            }
            FileOutputStream arquivoSaida = new FileOutputStream(nomeArquivo);
            workbook.write(arquivoSaida);
            arquivoSaida.close();
            System.out.println("Processo de modificação terminado!");
            return;
        }catch(Exception e){
            System.err.println("Erro ao fazer a leitura do arquivo " + LocalDateTime.now().format(formatter));
            e.printStackTrace();
        }



    }

    public Resultado infos(String token, String ticker) {
        Resultado resultado = null;
        System.out.println("Recebido: " + ticker);

        try {
            String url = "https://brapi.dev/api/quote/" + ticker
                    + "?token=" + token
                    + "&modules=summaryProfile";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String json = response.body();

            System.out.println("Resposta: " + ticker + " - " + json);


            if (json.contains("\"error\":true")) {

                json = "{ \"results\": [ { " +
                        "\"shortName\": \"Empresa não encontrada\", " +
                        "\"logourl\": \"Sem imagem\", " +
                        "\"summaryProfile\": { \"sector\": \"Sem setor\" } " +
                        "} ] }";
            }

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            resultado = objectMapper.readValue(json, Resultado.class);

        } catch (Exception e) {
            System.err.println("Erro ao processar ticker: " + ticker);
            e.printStackTrace();
        }

        return resultado;
    }



}
