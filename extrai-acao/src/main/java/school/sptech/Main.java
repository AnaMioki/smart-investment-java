package school.sptech;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        LeitorExcel exec = new LeitorExcel();
       exec.extrairAcoes("/home/ubuntu/proj/smart-investment-java/extrai-acao/src/main/resources/b3_stocks_1994_2020.xlsx");
    }
}