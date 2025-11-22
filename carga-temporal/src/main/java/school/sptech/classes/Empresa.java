package school.sptech.classes;

public class Empresa {

    private String ticker;
    private Integer id;

    public Empresa(String ticker, Integer id) {
        this.ticker = ticker;
        this.id = id;
    }

    public Empresa() {
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Empresa{" +
                "ticker='" + ticker + '\'' +
                ", id=" + id +
                '}';
    }
}