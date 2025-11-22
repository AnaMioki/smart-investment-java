package school.sptech;

public class Main {
    public static void main(String[] args) {

        S3Controller controller = new S3Controller();
        controller.baixarArquivo();
        ChamarSlack slack = new ChamarSlack();
        slack.tratarMensagem("--Iniciando execução do arquivo: Carga-empresa-banco---");


    }
}