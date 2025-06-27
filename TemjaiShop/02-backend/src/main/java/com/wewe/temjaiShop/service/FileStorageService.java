package com.wewe.temjaiShop.service;

import com.wewe.temjaiShop.config.UploadProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {

    private final UploadProperties uploadProperties;

    @Autowired
    public FileStorageService(UploadProperties uploadProperties) {
        this.uploadProperties = uploadProperties;
    }

    public Path getUploadPath(String filename) {
        return Paths.get(uploadProperties.getDir()).resolve(filename);
    }
}

