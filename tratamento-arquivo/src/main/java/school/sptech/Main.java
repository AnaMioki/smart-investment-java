package school.sptech;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        S3Controller controller = new S3Controller();
        ChamarSlack slack = new ChamarSlack();
        slack.tratarMensagem("---Iniciando execução do arquivo: Tratamento-arquivo---");
        controller.baixarArquivo();
    }

}