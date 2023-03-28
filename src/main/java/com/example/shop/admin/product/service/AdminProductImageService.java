package com.example.shop.admin.product.service;

import com.example.shop.admin.common.utils.SlugifyUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class AdminProductImageService {

    @Value("${app.uploadDir}")
    private String uploadDir;

    public String uploadImage(String filename, InputStream inputStream) {
        String newFileName = SlugifyUtils.slugifyFileName(filename);
        newFileName = ExistingFileRenameUtils.renameIfExist(Path.of(uploadDir), newFileName);

        if (!Files.exists(Path.of(uploadDir))) {
            try {
                Files.createDirectory(Path.of(uploadDir));
            } catch (IOException e) {
                throw new RuntimeException("Nie mogę zapisać katalogu", e);
            }
        }

        Path filePath = Paths.get(uploadDir).resolve(newFileName);
        try (OutputStream outputStream = Files.newOutputStream(filePath)) {
            inputStream.transferTo(outputStream);
        } catch (IOException e) {
            throw new RuntimeException("nie mogę zapisać pliku", e);
        }
        return newFileName;
    }


    public Resource serveFiles(String filename) {
        FileSystemResourceLoader fileSystemResourceLoader = new FileSystemResourceLoader();
        return fileSystemResourceLoader.getResource(uploadDir + filename);
    }
}
