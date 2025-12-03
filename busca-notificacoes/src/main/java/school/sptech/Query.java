package school.sptech;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Query {

    private final JdbcTemplate jdbcTemplate;
    private List<Informacoes> infos;

    public Query(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void carregarNotificacoes(){
        try{
           infos = jdbcTemplate.query("select a.idAcoesFavoritadas as id, a.fkAcoes as acao , e.ticker as ticker , a.fkUsuario as usuario, u.perfil, e.nome as nomeEmpresa,u.nome FROM acoesFavoritadas a join empresa e ON e.idEmpresa = a.fkAcoes JOIN  usuario u ON u.idUsuario = a.fkUsuario;  ", new BeanPropertyRowMapper<>(Informacoes.class));
        }catch (Exception e){
            System.out.println("Erro ao conseguir as informações das acoes favoritadas");
            System.out.println(e.getMessage());
        }

        System.out.println(Arrays.asList(infos));
        criarNotificacao(infos);

    }

    public void criarNotificacao (List<Informacoes> infos){
        for (Informacoes info : infos) {

            String mensagem;
            String queryMnsg;
            mensagem = switch (info.getPerfil()){
                case "Conservador" -> "%s, o retorno da ação %s (%s) variou: %s%% nos últimos 7 dias!";
                case "Arrojado"    -> "%s, o volume da ação %s (%s) variou: %s%% nos últimos 7 dias!";
                case "Moderado"    -> "%s, o preço da ação %s (%s) variou: %s%% nos últimos 7 dias!";
                default -> "Não conseguimos trazer as informações dessa ação ;-;";
            };

            queryMnsg = switch (info.getPerfil()){
                case "Conservador" -> "SELECT \n" +
                        "    ROUND(\n" +
                        "        (\n" +
                        "            (ult.precoFechamento - prim.precoAbertura) \n" +
                        "            / prim.precoAbertura\n" +
                        "        ) * 100, 2\n" +
                        "    ) AS retornoPercentual\n" +
                        "FROM\n" +
                        "    (SELECT precoAbertura \n" +
                        "     FROM acoes \n" +
                        "     WHERE fkEmpresa = %s \n" +
                        "     ORDER BY dtAtual DESC \n" +
                        "     LIMIT 1 OFFSET 6) AS prim\n" +
                        "JOIN\n" +
                        "    (SELECT precoFechamento \n" +
                        "     FROM acoes \n" +
                        "     WHERE fkEmpresa = %s \n" +
                        "     ORDER BY dtAtual DESC \n" +
                        "     LIMIT 1) AS ult;";
                case "Arrojado" -> "SELECT \n" +
                        "    ROUND(\n" +
                        "        (\n" +
                        "            (ult.volume - prim.volume) \n" +
                        "            / prim.volume\n" +
                        "        ) * 100, 2\n" +
                        "    ) AS variacaoVolume\n" +
                        "FROM\n" +
                        "    (SELECT volume \n" +
                        "     FROM acoes \n" +
                        "     WHERE fkEmpresa = %s \n" +
                        "     ORDER BY dtAtual DESC \n" +
                        "     LIMIT 1 OFFSET 6) AS prim\n" +
                        "JOIN\n" +
                        "    (SELECT volume \n" +
                        "     FROM acoes \n" +
                        "     WHERE fkEmpresa = %s \n" +
                        "     ORDER BY dtAtual DESC \n" +
                        "     LIMIT 1) AS ult;";
                case "Moderado" -> "SELECT \n" +
                        "    ROUND(\n" +
                        "        (\n" +
                        "            (ult.precoFechamento - prim.precoFechamento) \n" +
                        "            / prim.precoFechamento\n" +
                        "        ) * 100, 2\n" +
                        "    ) AS variacaoPrecoFechamento\n" +
                        "FROM\n" +
                        "    (SELECT precoFechamento \n" +
                        "     FROM acoes \n" +
                        "     WHERE fkEmpresa = %s \n" +
                        "     ORDER BY dtAtual DESC \n" +
                        "     LIMIT 1 OFFSET 6) AS prim\n" +
                        "JOIN\n" +
                        "    (SELECT precoFechamento \n" +
                        "     FROM acoes \n" +
                        "     WHERE fkEmpresa = %s \n" +
                        "     ORDER BY dtAtual DESC \n" +
                        "     LIMIT 1) AS ult;";
                default -> "select count(*) from usuario;";
            };


            Double valor = jdbcTemplate.queryForObject(queryMnsg.formatted(info.getAcao(), info.getAcao()),  Double.class);

            jdbcTemplate.update("INSERT INTO notificacoes VALUES (DEFAULT, 'Ação favoritada', DEFAULT, DEFAULT, ?, ?, ?)", mensagem.formatted(info.getNome(), info.getTicker(), info.getNomeEmpresa(), valor), info.getAcao(), info.getUsuario());

            System.out.println(valor);
            System.out.println(mensagem.formatted(info.getNome(), info.getTicker(), info.getNomeEmpresa(), valor));

        }
    }



}
