package school.sptech;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TratarExcel {
    private String token = System.getenv("TOKEN_API");


    public List<InfoTemporal> lerExcel(String nomeArquivo){
        List<InfoTemporal> lista = new ArrayList<>();
        ConexaoBanco con = new ConexaoBanco();

        try(InputStream arquivo = new FileInputStream(nomeArquivo);
            Workbook workbook = new XSSFWorkbook(arquivo)) {

            Sheet sheet = workbook.getSheetAt(0);

            System.out.println("Recebendo infos...");

            for (Row row : sheet) {
                if (row.getRowNum() % 100 == 0) {
                    System.out.println("Recebendo infos: " + row.getRowNum());
                }

                Cell nome = row.getCell(0);
                Cell setor = row.getCell(2);

                if (nome == null) continue;



                String conteudo = nome.toString();
                String contaudoSetor = setor.toString();

                    InfoAcao infoAcao = infos(conteudo , contaudoSetor);


                if(nome != null &&
                        setor != null &&
                        infoAcao != null &&
                        infoAcao.getFinancials() != null &&
                        infoAcao.getFinancials().getDividends() != null &&
                        infoAcao.getMarket_cap() != null &&
                        infoAcao.getFinancials().getEquity() != null &&
                        infoAcao.getFinancials().getEquity_per_share() != null &&
                        infoAcao.getFinancials().getDividends().getYield_12m() != null &&
                        infoAcao.getFinancials().getPrice_to_book_ratio() != null &&
                        retonaMultiploSetoria(setor.toString()) != null &&
                        retonaMultiploSetoria(setor.toString()) != 0){

                    InfoTemporal infoTemporal = new InfoTemporal(
                            conteudo,
                            infoAcao.getMarket_cap(),
                            infoAcao.getFinancials().getEquity(),
                            infoAcao.getFinancials().getEquity_per_share(),
                            retonaMultiploSetoria(contaudoSetor),
                            infoAcao.getFinancials().getDividends().getYield_12m(),
                            1.3,
                            infoAcao.getFinancials().getPrice_to_book_ratio(),
                            (infoAcao.getMarket_cap() / retonaMultiploSetoria(contaudoSetor)),
                            ((infoAcao.getMarket_cap() - infoAcao.getFinancials().getEquity()) / infoAcao.getFinancials().getEquity()),
                            2024);
                    lista.add(infoTemporal);
                }

            }

        } catch (RuntimeException | IOException e) {
            throw new RuntimeException(e);
        }


        Querys exec = new Querys(con.getJdbcTemplate(), con);
        exec.insereNome(lista);
        return lista;
    }

    public InfoAcao infos(String ticker, String setor) {

        InfoAcao infoTemporal = null;


        if(!(setor.equals("Sem setor"))){
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

            if (json.contains("\"error\":true")) {
                json = "{ \"results\": { \"" + ticker + "\": { " +
                        "\"name\": \"Empresa não encontrada\", " +
                        "\"sector\": \"Sem setor\", " +
                        "\"logo\": { \"big\": \"Sem imagem\" } " +
                        "} } }";
            }



            JsonObject root = JsonParser.parseString(json).getAsJsonObject();
            JsonObject results = root.getAsJsonObject("results");
            JsonObject infoTemporalJson = results.getAsJsonObject(ticker);
            infoTemporal = gson.fromJson(infoTemporalJson,InfoAcao.class );



            return infoTemporal;

        } catch (Exception e) {
            System.err.println("Erro ao processar ticker: " + ticker);
            e.printStackTrace();
        }
        }
        return infoTemporal;
    }


    public Integer retonaMultiploSetoria(String setor) {
        if (setor == null) return null;

        switch (setor.trim()) {
            case "Calçados": return 8;
            case "Bancos": return 11;
            case "Equipamentos Industriais": return 9;
            case "Produtos de Limpeza": return 7;
            case "Energia": return 10;
            case "Fios e Tecidos": return 6;
            case "Brinquedos e Jogos": return 5;
            case "Produtos para Construção": return 7;
            case "Siderurgia e Metalurgia": return 8;
            case "Petróleo, Gás e Biocombustíveis": return 12;
            case "Seguradoras": return 9;
            case "Incorporações": return 7;
            case "Água e Saneamento": return 10;
            case "Telecomunicações": return 8;
            case "Químicos": return 9;
            case "Mineração": return 7;
            case "Intermediários Financeiros": return 10;
            case "Produtos Diversos": return 6;
            case "Transporte Hidroviário": return 7;
            case "Construção Pesada": return 8;
            case "Exploração de Imóveis": return 7;
            case "Equipamentos de Construção e Agrícolas": return 9;
            case "Outros": return 5;
            case "Medicamentos": return 13;
            case "Máquinas e Equipamentos": return 9;
            case "Holdings Diversificadas": return 10;
            case "Material Rodoviário": return 9;
            case "Papel e Celulose": return 7;
            case "Vestuário e Calçados": return 8;
            case "Automóveis e Motocicletas": return 9;
            case "Bicicletas": return 6;
            case "Produção de Eventos e Shows": return 5;
            case "Engenharia Consultiva": return 6;
            case "Material Aeronáutico e Defesa": return 10;
            case "Equipamentos de Saúde": return 11;
            case "Hotelaria": return 8;
            case "Utensílios Domésticos": return 6;
            case "Madeira": return 7;
            case "Gás": return 10;
            case "Carnes e Derivados": return 7;
            case "Alimentos": return 8;
            case "Materiais Básicos": return 8;
            case "Construção e Engenharia": return 8;
            case "Saúde": return 12;
            case "Aluguel de carros": return 7;
            case "Softwares": return 15;
            case "Agricultura": return 6;
            case "Eletrodomésticos": return 7;
            case "Computadores e Equipamentos": return 10;
            case "Intermediação Imobiliária": return 7;
            case "Açucar e Alcool": return 8;
            case "Transporte Rodoviário": return 7;
            case "Transporte Aéreo": return 6;
            case "Exploração de Rodovias": return 10;

            default: return 5; // fallback
        }
    }

}
