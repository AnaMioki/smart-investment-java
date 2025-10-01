package school.sptech;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

// Representa o objeto summaryProfile
@JsonIgnoreProperties(ignoreUnknown = true)
public class SummaryProfile {

    @JsonProperty("industry")
    private String industry;

    @JsonProperty("sector")
    private String sector;

    public String getIndustry() {
        return industry;
    }

    public String getSector() {
        return sector;
    }

    @Override
    public String toString() {
        return "SummaryProfile{" +
                "industry='" + industry + '\'' +
                ", sector='" + sector + '\'' +
                '}';
    }
}
