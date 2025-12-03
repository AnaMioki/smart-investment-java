package school.sptech;

public class Informacoes {
    private Integer acao;
    private String ticker;
    private Integer usuario;
    private String nome;
    private String nomeEmpresa;
    private String perfil;

    public Informacoes(Integer acao, String ticker, Integer usuario, String nome, String nomeEmpresa,String perfil) {
        this.acao = acao;
        this.ticker = ticker;
        this.usuario = usuario;
        this.nome = nome;
        this.nomeEmpresa = nomeEmpresa;
        this.perfil = perfil;
    }

    public Informacoes() {
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }

    public Integer getAcao() {
        return acao;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public void setAcao(Integer acao) {
        this.acao = acao;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public Integer getUsuario() {
        return usuario;
    }

    public void setUsuario(Integer usuario) {
        this.usuario = usuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Informacoes{" +
                "acao=" + acao +
                ", ticker='" + ticker + '\'' +
                ", usuario=" + usuario +
                ", nome='" + nome + '\'' +
                ", nomeEmpresa='" + nomeEmpresa + '\'' +
                ", perfil='" + perfil + '\'' +
                '}';
    }
}
