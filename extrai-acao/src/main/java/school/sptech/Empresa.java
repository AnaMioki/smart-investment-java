package school.sptech;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Empresa {

    @JsonProperty("shortName")
    private String nome;

    @JsonProperty("logourl")
    private String urlImagem;

    @JsonProperty("summaryProfile")
    private SummaryProfile summaryProfile;

    public String getNome() {
        return nome;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public SummaryProfile getSummaryProfile() {
        return summaryProfile;
    }

    @Override
    public String toString() {
        return "Empresa{" +
                "nome='" + nome + '\'' +
                ", setor='" + (summaryProfile != null ? summaryProfile.getIndustry() : "N/A") + '\'' +
                ", urlImagem='" + urlImagem + '\'' +
                '}';
    }
}
