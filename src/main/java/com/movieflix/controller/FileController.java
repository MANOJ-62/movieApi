package com.movieflix.controller;

import com.movieflix.services.FileService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpResponse;

@RestController
@RequestMapping("/file/")
public class FileController {

    private FileService fileService;

    public FileController (FileService fileService) {
        this.fileService =  fileService;
    }

    @Value("${project.poster}")
    private String path;

    @PostMapping("upload")
    public ResponseEntity<String> UploadFileHandler(@RequestPart MultipartFile file) throws IOException {
        String UploadedFileName = fileService.uploadFile(path, file);
        return ResponseEntity.ok("File Uploaded Successfully : " + UploadedFileName);
    }

    @GetMapping(value = "/{fileName}")
    public void ServerFileHandler (@PathVariable String fileName, HttpServletResponse response) throws IOException {
        InputStream resourcePath= fileService.getResourceFile(path, fileName);
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        StreamUtils.copy(resourcePath, response.getOutputStream());
    }
}
