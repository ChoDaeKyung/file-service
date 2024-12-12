package com.example.fileservice.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

    @Service
    public class ImageService {

        public String getImage(String image) {

            // 이미지 경로 설정
            try {
                // 이미지 파일 경로 설정
                Path imagePath = Paths.get(image);
                String filename = image;
                Resource resource = new UrlResource(imagePath.toUri());

                if (resource.exists() && resource.isReadable()) {
                    // 파일을 읽어서 Base64로 인코딩
                    byte[] imageBytes = Files.readAllBytes(imagePath);
                    String base64Image = Base64.getEncoder().encodeToString(imageBytes);

                    // 이미지의 MIME 타입 설정
                    String contentType = null;
                    if (filename.toLowerCase().endsWith(".png")) {
                        contentType = MediaType.IMAGE_PNG_VALUE;
                    } else if (filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".jpeg")) {
                        contentType = MediaType.IMAGE_JPEG_VALUE;
                    }

                    String img = "data:" + contentType + ";base64," + base64Image;

                    return img;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return  null;
        }

    }
