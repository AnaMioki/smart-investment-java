package school.sptech;

public class Dividends {
    private Double yield_12m;
    private Double yield_12m_sum;

    public Double getYield_12m() {
        return yield_12m;
    }

    public Double getYield_12_sum() {
        return yield_12m_sum;
    }

    @Override
    public String toString() {
        return "Dividends{" +
                "yield_12m=" + yield_12m +
                " yield_12m_sum=" + yield_12m_sum +
                '}';
    }
}

