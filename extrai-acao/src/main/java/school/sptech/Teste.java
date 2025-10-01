package school.sptech;


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


            String responseBody = response.body();







        } catch (Exception e) {
            e.printStackTrace();
            return ;
        }
    }

    public static void main(String[] args) {
        Teste t = new Teste();
         t.infos(t.token, t.ticker);

    }

}
