package com.example.demo.services;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface IStoreageService {
    public String storeFile(MultipartFile file);
    public Stream<Path> loadAll(); //load all file inside a folder
    public byte[] readFileContent(String fileName);
    // Từ mảng byte xewm được các ảnh
    public void deleteAllFiles();
}
