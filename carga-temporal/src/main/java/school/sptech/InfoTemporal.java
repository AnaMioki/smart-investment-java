package school.sptech;

public class InfoTemporal {

    private String nome;
    private Double valorMercado;
    private Double partrimonioLiquido;
    private Double patrimonioLiquidoAcao;
    private Integer multiploSetorial;
    private Double rentabilidadeAnual;
    private Double infoTemporalcol;
    private Double precoSobreValorPatrimonial;
    private Double EBTDA;
    private Double DRE;
    private Integer ano;


    public InfoTemporal(String nome, Double valorMercado, Double partrimonioLiquido, Double patrimonioLiquidoAcao, Integer multiploSetorial, Double rentabilidadeAnual, Double infoTemporalcol, Double precoSobreValorPatrimonial, Double EBTDA, Double DRE, Integer ano) {
        this.nome = nome;
        this.valorMercado = valorMercado;
        this.partrimonioLiquido = partrimonioLiquido;
        this.patrimonioLiquidoAcao = patrimonioLiquidoAcao;
        this.multiploSetorial = multiploSetorial;
        this.rentabilidadeAnual = rentabilidadeAnual;
        this.infoTemporalcol = infoTemporalcol;
        this.precoSobreValorPatrimonial = precoSobreValorPatrimonial;
        this.EBTDA = EBTDA;
        this.DRE = DRE;
        this.ano = ano;
    }

    // Getters

    public String getNome() {
        return nome;
    }

    public Double getValorMercado() {
        return valorMercado;
    }

    public Double getPartrimonioLiquido() {
        return partrimonioLiquido;
    }

    public Double getPatrimonioLiquidoAcao() {
        return patrimonioLiquidoAcao;
    }

    public Integer getMultiploSetorial() {
        return multiploSetorial;
    }

    public Double getRentabilidadeAnual() {
        return rentabilidadeAnual;
    }

    public Double getInfoTemporalcol() {
        return infoTemporalcol;
    }

    public Double getPrecoSobreValorPatrimonial() {
        return precoSobreValorPatrimonial;
    }

    public Double getEBTDA() {
        return EBTDA;
    }

    public Double getDRE() {
        return DRE;
    }

    public Integer getAno() {
        return ano;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setValorMercado(Double valorMercado) {
        this.valorMercado = valorMercado;
    }

    public void setPartrimonioLiquido(Double partrimonioLiquido) {
        this.partrimonioLiquido = partrimonioLiquido;
    }

    public void setPatrimonioLiquidoAcao(Double patrimonioLiquidoAcao) {
        this.patrimonioLiquidoAcao = patrimonioLiquidoAcao;
    }

    public void setMultiploSetorial(Integer multiploSetorial) {
        this.multiploSetorial = multiploSetorial;
    }

    public void setRentabilidadeAnual(Double rentabilidadeAnual) {
        this.rentabilidadeAnual = rentabilidadeAnual;
    }

    public void setInfoTemporalcol(Double infoTemporalcol) {
        this.infoTemporalcol = infoTemporalcol;
    }

    public void setPrecoSobreValorPatrimonial(Double precoSobreValorPatrimonial) {
        this.precoSobreValorPatrimonial = precoSobreValorPatrimonial;
    }

    public void setEBTDA(Double EBTDA) {
        this.EBTDA = EBTDA;
    }

    public void setDRE(Double DRE) {
        this.DRE = DRE;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    @Override
    public String toString() {
        return "InfoTemporal{" +
                "valorMercado=" + valorMercado +
                ", partrimonioLiquido=" + partrimonioLiquido +
                ", patrimonioLiquidoAcao=" + patrimonioLiquidoAcao +
                ", multiploSetorial=" + multiploSetorial +
                ", rentabilidadeAnual=" + rentabilidadeAnual +
                ", infoTemporalcol=" + infoTemporalcol +
                ", precoSobreValorPatrimonial=" + precoSobreValorPatrimonial +
                ", EBTDA=" + EBTDA +
                ", DRE=" + DRE +
                ", ano=" + ano +
                '}';
    }
}
