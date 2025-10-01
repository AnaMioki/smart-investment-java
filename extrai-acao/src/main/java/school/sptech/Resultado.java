package school.sptech;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

// Classe raiz que representa a resposta da API
@JsonIgnoreProperties(ignoreUnknown = true)
public class Resultado {

    @JsonProperty("results")
    private List<Empresa> results;

    public List<Empresa> getResults() {
        return results;
    }

    public void setResults(List<Empresa> results) {
        this.results = results;
    }
}
