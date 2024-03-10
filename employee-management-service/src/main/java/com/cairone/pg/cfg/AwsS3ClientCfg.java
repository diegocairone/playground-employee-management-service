package com.cairone.pg.cfg;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@Configuration
public class AwsS3ClientCfg {

    @Value("${app.store.aws-endpoint}")
    private String awsEndpoint;
    @Value("${app.store.aws-access-key-id}")
    private String awsAccessKeyId;
    @Value("${app.store.aws-secret-access-key}")
    private String awsSecretAccessKey;
    @Value("${app.store.aws-region}")
    private String awsRegion;

    private AwsBasicCredentials credentials;

    private AwsBasicCredentials getCredentials() {
        if (credentials == null) {
            this.credentials = AwsBasicCredentials.create(awsAccessKeyId, awsSecretAccessKey);
        }
        return credentials;
    }

    @Bean
    public S3Client getS3Client() {

        var builder = S3Client.builder()
                .region(Region.of(awsRegion))
                .credentialsProvider(StaticCredentialsProvider.create(getCredentials()))
                .forcePathStyle(true);

        if (!awsEndpoint.isBlank()) {
            builder.endpointOverride(URI.create(awsEndpoint));
        }

        return builder.build();
    }

    @Bean
    public S3Presigner getPresigner() {
        var builder = S3Presigner.builder()
                .s3Client(getS3Client())
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build())
                .credentialsProvider(StaticCredentialsProvider.create(getCredentials()));

        if (!awsEndpoint.isBlank()) {
            builder.endpointOverride(URI.create(awsEndpoint));
        }

        return builder.build();
    }
}
