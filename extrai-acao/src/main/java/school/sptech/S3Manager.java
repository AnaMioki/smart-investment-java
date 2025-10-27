package school.sptech;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class S3Manager {
    private final S3Client s3Client;
    private final String bucketName;

    public S3Manager(String accessKey, String secretKey, String bucketName) {
        this.s3Client = new S3Provider(accessKey, secretKey).getS3Client();
        this.bucketName = bucketName;
    }

    public void createBucketIfNotExists() {
        try {
            s3Client.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
            System.out.println("✅ Bucket criado: " + bucketName);
        } catch (S3Exception e) {
            System.out.println("ℹ️  Bucket já existe: " + bucketName);
        }
    }

    public String uploadFile(File file) {
        String key = UUID.randomUUID().toString() + "_" + file.getName();
        return uploadFile(file, key);
    }

    public String uploadFile(File file, String key) {
        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(bucketName).key(key).build(),
                RequestBody.fromFile(file));
        System.out.println("✅ Arquivo enviado para S3: " + key);
        return key;
    }

    public void downloadFile(String key, String localPath) {
        s3Client.getObject(GetObjectRequest.builder()
                        .bucket(bucketName).key(key).build(),
                ResponseTransformer.toFile(new File(localPath)));
        System.out.println("✅ Arquivo baixado: " + localPath);
    }
}