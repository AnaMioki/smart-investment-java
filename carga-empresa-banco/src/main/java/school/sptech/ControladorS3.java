package school.sptech;

import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ControladorS3 {
    private S3Client s3Client = new S3Provider().getS3Client();
    private ConexaoBanco con = new ConexaoBanco();
    private GuardaLog log = new GuardaLog(con.getJdbcTemplate());
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private LocalDateTime dataAtual = LocalDateTime.now();
    private String bucketName = "smart-investment-bucket";
    private LeArquivo exec = new LeArquivo();

    public void baixarArquivos(){
        try {
            ListObjectsRequest requisicao = ListObjectsRequest.builder()
                    .bucket(bucketName)
                    .build();
            List<S3Object> objects = s3Client.listObjects(requisicao).contents();
            for (S3Object object : objects) {
                if(object.key().contains("ListaAcao")){
                    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                            .bucket(bucketName)
                            .key(object.key())
                            .build();
                    InputStream inputStream = s3Client.getObject(getObjectRequest, ResponseTransformer.toInputStream());
                    Files.copy(inputStream, new File(object.key()).toPath());
                    System.out.println("Arquivo baixado: " + object.key());
                    log.gardaLog("Sucesso", dataAtual.format(formatter), "Sucesso ao baixar o arquivo das empresas do bucket S3 - JAR: Carga empreas banco");
                    exec.extrairEmpresa(object.key().toString());
                }
            }
        } catch (IOException | S3Exception e) {
            System.err.println("Erro ao fazer download dos arquivos: " + e.getMessage());
        }

    }

}
