package com.movieflix.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadFile(String path, MultipartFile file) throws IOException {
        //get name of the file
        String fileName = file.getOriginalFilename();
        //get path of the file
        String filepath = path + File.separator + fileName;
        // crate a file object
        File f = new File(path);
        if (!f.exists()){
            f.mkdir();
        }

        //copy or upload file
        Files.copy(file.getInputStream(), Paths.get(filepath));
        return fileName;
    }

    @Override
    public InputStream getResourceFile(String path, String filename) throws FileNotFoundException {
        String filePath = path + File.separator + filename;
        return new FileInputStream(filePath);
    }
}
