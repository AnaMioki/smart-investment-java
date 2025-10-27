package school.sptech;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

public class S3Provider {
    private final StaticCredentialsProvider credentialsProvider;

    public S3Provider(String accessKey, String secretKey) {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);
        this.credentialsProvider = StaticCredentialsProvider.create(awsCreds);
    }

    public S3Client getS3Client() {
        return S3Client.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(credentialsProvider)
                .build();
    }
}