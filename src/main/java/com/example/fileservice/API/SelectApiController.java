package com.example.fileservice.API;

import com.example.fileservice.service.ImageService;
import com.example.fileservice.service.S3ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class SelectApiController {

    private final ImageService imageService;
    private final S3ImageService s3ImageService;

    @PostMapping("/selectImage")
    public List<String> getImage(@RequestBody List<String> imagePaths){
        System.out.println("imagePaths : " + imagePaths);

        List<String> encodedImages = imagePaths.stream()
                .map(s3ImageService::getImage) // ImageService를 통해 Base64 인코딩
                .collect(Collectors.toList()); // 결과를 List로 수집

        return encodedImages;
    }


}
