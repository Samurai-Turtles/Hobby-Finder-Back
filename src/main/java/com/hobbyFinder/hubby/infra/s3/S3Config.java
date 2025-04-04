package com.hobbyFinder.hubby.infra.s3;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.auth.signer.AwsS3V4Signer;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.core.client.config.SdkAdvancedClientOption;
import software.amazon.awssdk.http.SdkHttpClient;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
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
        SdkHttpClient httpClient = ApacheHttpClient.builder().build();

        return S3Client.builder()
                .endpointOverride(URI.create(s3Endpoint))
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                ))
                .httpClient(httpClient)
                .serviceConfiguration(s3 -> s3.pathStyleAccessEnabled(true)) // <--- ESSENCIAL PARA MINIO
                .overrideConfiguration(ClientOverrideConfiguration.builder()
                        .putAdvancedOption(SdkAdvancedClientOption.SIGNER, AwsS3V4Signer.create())
                        .build())
                .build();
    }

}