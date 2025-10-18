package school.sptech;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

public class TratarExcel {
    private String token = "5c949faa";



    public void tratamentoDados(String nomeArquivo){
        LocalDateTime dataAtual = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        ConexaoBanco con = new ConexaoBanco();
        GuardaLog log = new GuardaLog(con.getJdbcTemplate());



        try(InputStream arquivo = new FileInputStream(nomeArquivo);
            Workbook workbook = new XSSFWorkbook(arquivo)){

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {


                Cell cell = row.getCell(0);

                if(cell == null) continue;

                String conteudo = cell.toString();

                AcaoResumo resultado = infos(token, conteudo);



                Cell cellNome = row.createCell(1);
                Cell cellSetor = row.createCell(2);
                Cell cellUrl = row.createCell(3);

                cellNome.setCellValue(resultado.getName());

                cellSetor.setCellValue(
                        (resultado.getSector() != null)
                                ? resultado.getSector()
                                : "Sem setor"
                );

                cellUrl.setCellValue(
                        (resultado.getLogo() != null && resultado.getLogo().getBig() != null)
                                ? resultado.getLogo().getBig()
                                : "Sem imagem"
                );

            }
            FileOutputStream arquivoSaida = new FileOutputStream(nomeArquivo);
            workbook.write(arquivoSaida);
            arquivoSaida.close();
            System.out.println("Processo de modificação terminado!" + LocalDateTime.now().format(formatter));
            log.gardaLog("Sucesso" , dataAtual.format(formatter), "Sucesso ao modificar o arquivo!");


            return;
        }catch(Exception e){
            System.err.println("Erro ao fazer a leitura do arquivo " + LocalDateTime.now().format(formatter));
            e.printStackTrace();
            log.gardaLog("Erro" , dataAtual.format(formatter), "Erro ao fazer a leitura do arquivo! \n" + e.getMessage());
        }



    }

    public AcaoResumo infos(String token, String ticker) {

        System.out.println("Recebido: " + ticker);
        AcaoResumo acao = null;

        try {
            String url = "https://api.hgbrasil.com/finance/stock_price?key=" + token
                    + "&symbol=" + ticker;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String json = response.body();

            Gson gson = new Gson();
            System.out.println(json);

            if (json.contains("\"error\":true")) {
                json = "{ \"results\": { \"" + ticker + "\": { " +
                        "\"name\": \"Empresa não encontrada\", " +
                        "\"sector\": \"Sem setor\", " +
                        "\"logo\": { \"big\": \"Sem imagem\" } " +
                        "} } }";
            }

            System.out.println(json);

            JsonObject root = JsonParser.parseString(json).getAsJsonObject();
            JsonObject results = root.getAsJsonObject("results");
            JsonObject acaoJson = results.getAsJsonObject(ticker);
            acao = gson.fromJson(acaoJson,AcaoResumo.class );

            return acao;

        } catch (Exception e) {
            System.err.println("Erro ao processar ticker: " + ticker);
            e.printStackTrace();
        }

        return acao;
    }



}
