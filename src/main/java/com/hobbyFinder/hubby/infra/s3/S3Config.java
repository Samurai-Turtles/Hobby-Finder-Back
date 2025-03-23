package com.hobbyFinder.hubby.infra.s3;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
@NoArgsConstructor
@AllArgsConstructor
public class S3Config {

    @Value("${FAKE_AWS_ENDPOINT}")
    private String s3Endpoint;

    @Value("${FAKE_AWS_USER}")
    private String accessKey;

    @Value("${FAKE_AWS_PSWD}")
    private String secretKey;

    @Value("${FAKE_AWS_BUCKET}")
    private String bucketName;

    @Value("${FAKE_AWS_REGION}")
    private String region;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .endpointOverride(URI.create(s3Endpoint)) // Endpoint do MinIO
                .region(Region.of(region)) // Região do S3
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                ))
                .build();
    }

}