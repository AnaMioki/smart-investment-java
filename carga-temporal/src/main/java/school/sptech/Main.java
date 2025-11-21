package school.sptech;

public class Main {
    public static void main(String[] args) {

        //TratarExcel tratar = new TratarExcel();
        //tratar.infos("be736140", "PETR3");

        S3Controller controller = new S3Controller();
        controller.baixarArquivo();
    }
}
