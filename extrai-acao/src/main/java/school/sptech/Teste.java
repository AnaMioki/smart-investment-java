package school.sptech;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Teste {
    private String token = "dbESckEYvyLTKmNAE9HL3v";
    String ticker = "PETR4";

    public void infos(String token, String ticker) {
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

            Resultado resultado = objectMapper.readValue(json, Resultado.class);

            System.out.println(resultado.getResults());

            System.out.println("Nome: " + resultado.getResults().get(0).getNome().replace(" ", "-"));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Teste t = new Teste();
        t.infos(t.token, t.ticker);
    }
}
