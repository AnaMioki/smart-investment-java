package school.sptech;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {


        ControladorS3 controller = new ControladorS3();

        //Iniciando processo

        controller.baixarArquivos();

//        LeitorExcel exec = new LeitorExcel();
//        exec.extrairAcoes("/home/ubuntu/proj/smart-investment-java/extrai-acao/b3_stocks_1994_2020.xlsx");
    }
}