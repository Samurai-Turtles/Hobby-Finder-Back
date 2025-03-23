package com.hobbyFinder.hubby.infra.s3;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Component
public class PhotoRepository {

    private S3Client s3Client; // Nome do bucket

    public PhotoRepository(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public void upload(UUID id, MultipartFile file) {
        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(Constants.BUCKET)
                            .key(id.toString())
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromBytes(file.getBytes())
            );
        } catch (IOException e) {
            throw new RuntimeException("Erro ao enviar o arquivo", e);
        }
    }

    public byte[] getFile(UUID id) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(Constants.BUCKET)
                .key(id.toString())
                .build();

        return s3Client.getObjectAsBytes(getObjectRequest).asByteArray();
    }
}
