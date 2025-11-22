package school.sptech.classes;

public class Financials {
    private Double equity;
    private Double equity_per_share;
    private Double price_to_book_ratio;
    private Dividends dividends;

    public Dividends getDividends() {
        return dividends;
    }

    public Double getEquity() {
        return equity;
    }

    public Double getEquity_per_share() {
        return equity_per_share;
    }

    public Double getPrice_to_book_ratio() {
        return price_to_book_ratio;
    }

    @Override
    public String toString() {
        return "Financials{" +
                "equity=" + equity +
                ", equity_per_share=" + equity_per_share +
                ", price_to_book_ratio=" + price_to_book_ratio +
                '}';
    }
}
