package school.sptech.controllers;

import school.sptech.InfoTemporal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeradorHistorico {

    private static double rand(Random r, double min, double max) {
        return min + (max - min) * r.nextDouble();
    }

    public static List<InfoTemporal> gerarSerieHistorica(InfoTemporal base2024) {

        List<InfoTemporal> lista = new ArrayList<>();
        lista.add(base2024); // mantém o real (ano 2024)

        Random r = new Random();
        InfoTemporal anoPosterior = base2024;


        for (int ano = 2023; ano >= 2007; ano--) {

            double fator;

            if (ano == 2020) fator = rand(r, -0.30, -0.15);       // pandemia forte
            else if (ano == 2021) fator = rand(r, -0.15, -0.05);  // pandemia moderada
            else if (ano == 2022) fator = rand(r, 0.02, 0.10);    // recuperação forte
            else if (ano == 2023) fator = rand(r, 0.02, 0.08);    // recuperação leve
            else fator = rand(r, -0.05, 0.05);                    // anos normais

            InfoTemporal novo = new InfoTemporal(
                    anoPosterior.getNome(),
                    anoPosterior.getValorMercado() * (1 + fator),
                    anoPosterior.getPartrimonioLiquido() * (1 + fator),
                    anoPosterior.getPatrimonioLiquidoAcao() * (1 + fator),
                    anoPosterior.getMultiploSetorial(),
                    anoPosterior.getRentabilidadeAnual() * (1 + fator),
                    anoPosterior.getInfoTemporalcol() * (1 + fator),
                    anoPosterior.getPrecoSobreValorPatrimonial() * (1 + fator),
                    anoPosterior.getEBTDA() * (1 + fator),
                    anoPosterior.getDRE() * (1 + fator),
                    ano
            );

            lista.add(novo);
            anoPosterior = novo;
        }

        return lista;
    }
}
