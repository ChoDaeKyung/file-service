package com.example.fileservice.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class S3ImageService {

    private final S3Client s3Client;

    public String getImage(String imageUrl) {
        try {
            // S3 URL에서 버킷 이름과 키 추출
            String bucketName = extractBucketName(imageUrl);
            String key = extractKey(imageUrl);

            // S3 객체 가져오기
            byte[] imageBytes = s3Client.getObjectAsBytes(
                    GetObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .build()
            ).asByteArray();

            // Base64로 인코딩
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            // Content-Type 추출 (S3에서 제공된 데이터는 별도로 Content-Type 관리 필요)
            String contentType = determineContentType(key);

            return "data:" + contentType + ";base64," + base64Image;
        } catch (Exception e) {
            throw new RuntimeException("Failed to process image from S3", e);
        }
    }

    private String extractBucketName(String url) {
        return url.split("\\.s3\\.")[0].split("https://")[1];
    }

    private String extractKey(String url) {
        return url.split(".amazonaws.com/")[1];
    }

    private String determineContentType(String key) {
        if (key.endsWith(".png")) {
            return "image/png";
        } else if (key.endsWith(".jpg") || key.endsWith(".jpeg")) {
            return "image/jpeg";
        }
        return "application/octet-stream";
    }
}