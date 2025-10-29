package school.sptech;

import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class S3Controller {


    private final String bucketName = "smart-investment-bucket";
    private final S3Client s3Client = new S3Provider().getS3Client();
    private String nomeObjeto;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final LocalDateTime dataAtual = LocalDateTime.now();
    private final ConexaoBanco con = new ConexaoBanco();
    private final GuardaLog log = new GuardaLog(con.getJdbcTemplate());
    LeitorExcel exec = new LeitorExcel();



    public void baixarArquivo(){
        boolean arquivoEncontrado = false;

        try {
            ListObjectsRequest requisicao = ListObjectsRequest.builder()
                    .bucket(bucketName)
                    .build();

            List<S3Object> objects = s3Client.listObjects(requisicao).contents();

            for (S3Object object : objects) {
                if (object.key().contains("b3_stocks_1994_2020")) {
                    nomeObjeto = object.key();
                    File arquivoLocal = new File(nomeObjeto);
                    if (!arquivoLocal.exists()) {
                        System.out.println("Baixando arquivo do S3...");
                        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                                .bucket(bucketName)
                                .key(nomeObjeto)
                                .build();

                        try (InputStream inputStream = s3Client.getObject(getObjectRequest, ResponseTransformer.toInputStream())) {
                            Files.copy(inputStream, arquivoLocal.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        }

                        System.out.println("Arquivo baixado: " + nomeObjeto);
                        log.gardaLog("Sucesso", dataAtual.format(formatter), "Download concluído: " + nomeObjeto);
                    } else {
                        System.out.println("Arquivo já existente localmente, pulando download: " + nomeObjeto);
                        log.gardaLog("Alerta", dataAtual.format(formatter), "Arquivo já existente, continuando o processo: " + nomeObjeto);
                    }
                    exec.extrairAcoes(nomeObjeto);
                    arquivoEncontrado = true;
                    break;
                }
            }

            if (!arquivoEncontrado) {
                System.err.println("Nenhum arquivo contendo 'ListaAcao' foi encontrado no bucket.");
                log.gardaLog("Erro", dataAtual.format(formatter), "Nenhum arquivo 'ListaAcao' encontrado no bucket.");
                return;
            }

        } catch (S3Exception e) {
            System.err.println("Erro ao acessar o bucket S3: " + e.getMessage());
            log.gardaLog("Erro", dataAtual.format(formatter), "Erro ao acessar bucket S3: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erro ao processar arquivo: " + e.getMessage());
            log.gardaLog("Erro", dataAtual.format(formatter), "Erro ao processar arquivo: " + e.getMessage());
        }

    }


    public void excluirArquivo() {
        File arquivo = new File(nomeObjeto);
        if (arquivo.exists()) {
            if (arquivo.delete()) {
                System.out.println("Arquivo local deletado com sucesso: " + nomeObjeto);
            } else {
                System.err.println("Falha ao deletar o arquivo local: " + nomeObjeto);
            }
        } else {
            System.out.println("Arquivo já não existe localmente: " + nomeObjeto);
        }
    }


    }
