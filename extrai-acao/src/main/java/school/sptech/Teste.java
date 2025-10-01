package school.sptech;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class Teste {
    private String token = "dbESckEYvyLTKmNAE9HL3v";
    String ticker = "PETR4";


    public String infos(String token, String ticker) {

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

            return response.body();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        Teste t = new Teste();
        String resposta = t.infos(t.token, t.ticker);
        System.out.println(resposta);
    }

}
