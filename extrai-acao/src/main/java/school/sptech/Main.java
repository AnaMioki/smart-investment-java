package school.sptech;

public class Main {
    public static void main(String[] args) {
        ControladorS3 controller = new ControladorS3();
        ChamarSlack slack = new ChamarSlack();
        slack.tratarMensagem("--Iniciando execução do arquivo: Extrai-acao---");
        controller.baixarArquivos();
    }
}