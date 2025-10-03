package school.sptech;

public class Main {
    public static void main(String[] args) {

        String nomeArquivo = "../ListaAcao.xlsx";
        LeArquivo exec = new LeArquivo();

        exec.extrairEmpresa(nomeArquivo);

    }
}