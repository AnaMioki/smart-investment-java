package school.sptech;


public class Main {
    public static void main(String[] args) {

        ConexaoBanco con = new ConexaoBanco();
        Query iniciar = new Query(con.getJdbcTemplate());

        iniciar.carregarNotificacoes();
    }
}