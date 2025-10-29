package school.sptech;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ControladorS3 {
    private S3Client s3Client = new S3Provider().getS3Client();
    private ConexaoBanco con = new ConexaoBanco();
    private GuardaLog log = new GuardaLog(con.getJdbcTemplate());
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private LocalDateTime dataAtual = LocalDateTime.now();
    private String nomeObjeto;
    private String bucketName = "smart-investment-bucket";

    public String getNomeObjeto() {
        return nomeObjeto;
    }

    public void setNomeObjeto(String nomeObjeto) {
        this.nomeObjeto = nomeObjeto;
    }

    public void baixarArquivos() {
        LeitorExcel ler = new LeitorExcel();
        Boolean achou = false;

        try {
            ListObjectsRequest requisicao = ListObjectsRequest.builder()
                    .bucket(bucketName)
                    .build();

            List<S3Object> objects = s3Client.listObjects(requisicao).contents();
            System.out.println("Objetos no bucket " + bucketName + ":");
            for (S3Object object : objects) {
                System.out.println("- " + object.key());
                if (object.key().contains("b3_stocks_1994_2020")) {
                    achou = true;
                    setNomeObjeto(object.key().toString());
                }

                if (!(object.key().contains("b3_stocks_1994_2020"))) {
                    System.out.println("Excluindo arquivos antes de iniciar o processo");
                    excluirArquivos(object.key().toString());
                }
            }
            if (achou == true) {
                try {
                    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                            .bucket(bucketName)
                            .key(getNomeObjeto())
                            .build();

                    System.out.println("Baixando arquivo...");
                    InputStream inputStream = s3Client.getObject(getObjectRequest, ResponseTransformer.toInputStream());
                    Files.copy(inputStream, new File(getNomeObjeto()).toPath());
                    System.out.println("Arquivo baixado: " + getNomeObjeto());
                    log.gardaLog("Sucesso", dataAtual.format(formatter), "Sucesso ao fazer o dowload do arquivo \n");
                    ler.extrairAcoes(getNomeObjeto());
                } catch (Exception e) {
                    log.gardaLog("Alerta", dataAtual.format(formatter), "Arquivo já instalado, continuando o processo \n");
                    System.out.println("Arquivo já existente! Continuando o processo");
                    ler.extrairAcoes(getNomeObjeto());
                }
            } else {
                System.err.println("Erro ao baixar objeto no bucket: Arquivo não encontrado ");
                log.gardaLog("Erro", dataAtual.format(formatter), "Erro ao baixar objeto no bucket: Arquivo não encontrado:\n");
            }

        } catch (S3Exception e) {
            System.err.println("Erro ao listar objetos no bucket: " + e.getMessage());
            log.gardaLog("Erro", dataAtual.format(formatter), "Erro ao listar objetos no bucket:\n" + e.getMessage());
            return;
        }

    }

    public void excluirArquivos(String nome) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(nome)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);

            System.out.println("Objeto deletado com sucesso: " + nome);
        } catch (S3Exception e) {
            System.err.println("Erro ao deletar objeto: " + e.getMessage());
        }
    }


    public void subirNovoArquivo(String nomeArquivo) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(nomeArquivo)
                    .build();

            File file = new File(nomeArquivo);
            s3Client.putObject(putObjectRequest, RequestBody.fromFile(file));

            excluirArquivoLocal();
            System.out.println("Arquivo '" + file.getName() + "' enviado com sucesso com o nome: " + nomeArquivo);

        } catch (S3Exception e) {
            System.err.println("Erro ao fazer upload do arquivo: " + e.getMessage());
        }
    }


    public void excluirArquivoLocal() {
        try {
            ListObjectsRequest requisicao = ListObjectsRequest.builder().bucket(bucketName).build();
            List<S3Object> objects = s3Client.listObjects(requisicao).contents();
            System.out.println("Limpando arquivos locais com base no bucket " + bucketName + ":");
            for (S3Object object : objects) {
                File arquivo = new File("./" + object.key());
                if (arquivo.exists()) {
                    if (arquivo.delete()) {
                        System.out.println("Arquivo local deletado com sucesso: " + arquivo.getName());
                    } else {
                        System.err.println("Falha ao deletar arquivo local: " + arquivo.getName());
                    }
                }
            }
        } catch (S3Exception e) {
            System.err.println("Erro ao listar objetos no bucket: " + e.getMessage());
        }
    }


}
