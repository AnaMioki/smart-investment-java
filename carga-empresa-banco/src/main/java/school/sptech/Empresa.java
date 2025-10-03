package school.sptech;

public class Empresa {

    private String nome;
    private String setor;
    private String imagem;
    private String ticker;

    public Empresa(String nome, String setor, String imagem, String ticker) {
        this.nome = nome;
        this.setor = setor;
        this.imagem = imagem;
        this.ticker = ticker;
    }

    public Empresa() {
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return "Empresa{" +
                "nome='" + nome + '\'' +
                ", setor='" + setor + '\'' +
                ", imagem='" + imagem + '\'' +
                ", ticker='" + ticker + '\'' +
                '}';
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }
}
