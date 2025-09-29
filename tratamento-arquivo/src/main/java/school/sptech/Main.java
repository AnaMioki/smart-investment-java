package school.sptech;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        ConexaoBanco con = new ConexaoBanco();

        Querys exec = new Querys(con.getJdbcTemplate());

        LeitorExcel exec1 = new LeitorExcel();
        exec1.extrairAcoes("b3_stocks_1994_2020.xlsx");
    }

}