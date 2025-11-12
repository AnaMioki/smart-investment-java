package school.sptech;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        S3Controller controller = new S3Controller();
        ConexaoBanco con = new ConexaoBanco();

        Querys exec = new Querys(con.getJdbcTemplate(), con);
        controller.baixarArquivo();
    }

}