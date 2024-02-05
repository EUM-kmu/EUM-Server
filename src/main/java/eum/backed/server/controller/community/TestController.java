package eum.backed.server.controller.community;

import eum.backed.server.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestController {
    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<Object> uploadFilesSample(
            @RequestPart(value = "files") List<MultipartFile> multipartFiles) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(fileService.uploadFiles(multipartFiles));
    }
}
