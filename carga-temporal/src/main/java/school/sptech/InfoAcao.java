package school.sptech;

public class InfoAcao
{
    private Double price;
    private String symbol;
    private Double market_cap;
    private Financials financials;


    public String getSymbol() {
        return symbol;
    }

    public Financials getFinancials() {
        return financials;
    }

    public Double getPrice() {
        return price;
    }

    public Double getMarket_cap() {
        return market_cap;
    }

    @Override
    public String toString() {
        return "InfoAcao{" +
                "price=" + price +
                ", symbol='" + symbol + '\'' +
                ", market_cap=" + market_cap +
                ", financials=" + financials +
                '}';
    }
}
