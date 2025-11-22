package school.sptech;

import school.sptech.controllers.ChamarSlack;
import school.sptech.controllers.S3Controller;

public class Main {
    public static void main(String[] args) {

        S3Controller controller = new S3Controller();
        ChamarSlack chamar = new ChamarSlack();
        chamar.tratarMensagem("---Iniciando execução do arquivo: Carga-temporal---");
        controller.baixarArquivo();
    }
}
