package school.sptech;

public class Acao {
    private String data;
    private String ticker;
    private Double Abertura;
    private Double Fechamento;
    private Double Alta;
    private Double Baixa;
    private Double volume;

    public Acao(String data, String ticker, Double abertura, Double fechamento, Double alta, Double baixa, Double volume) {
        this.data = data;
        this.ticker = ticker;
        Abertura = abertura;
        Fechamento = fechamento;
        Alta = alta;
        Baixa = baixa;
        this.volume = volume;
    }

    public Acao() {
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public Double getAbertura() {
        return Abertura;
    }

    public void setAbertura(Double abertura) {
        Abertura = abertura;
    }

    public Double getFechamento() {
        return Fechamento;
    }

    public void setFechamento(Double fechamento) {
        Fechamento = fechamento;
    }

    public Double getAlta() {
        return Alta;
    }

    public void setAlta(Double alta) {
        Alta = alta;
    }

    public Double getBaixa() {
        return Baixa;
    }

    public void setBaixa(Double baixa) {
        Baixa = baixa;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "Acao{" +
                "data='" + data + '\'' +
                ", ticker='" + ticker + '\'' +
                ", Abertura=" + Abertura +
                ", Fechamento=" + Fechamento +
                ", Alta=" + Alta +
                ", Baixa=" + Baixa +
                ", volume=" + volume +
                '}';
    }
}
