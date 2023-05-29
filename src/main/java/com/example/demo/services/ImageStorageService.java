package com.example.demo.services;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class ImageStorageService implements IStoreageService{
    private final Path storageFolder = Paths.get("uploads");
    public ImageStorageService(){
        try{
            Files.createDirectories(storageFolder);
        }catch (IOException e){
            throw new RuntimeException("Cannot initialize storage", e);
        }
    }
    private boolean isImageFile(MultipartFile file){
        // Kiem tra xem co phai la file Image khong
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        return Arrays.asList(new String[]{"png", "jpg", "jpeg", "bmp"})
                .contains(fileExtension.trim().toLowerCase());
    }
    @Override
    public String storeFile(MultipartFile file) {
        try {
            System.out.println("hihi");
            if(file.isEmpty()){
                throw new RuntimeException("Failed to store empty file.");
            }
            //check file is image ?
            if(!isImageFile(file)){
                throw new RuntimeException("You can only upload image file");
            }
            //file must be <= 5MB
            float fileSizeInMegabytes = file.getSize()/ 1_000_000.0f;
            if (fileSizeInMegabytes > 5.0f){
                throw new RuntimeException("File must be <= 5Mb");
            }
            //File must be re name on Server
            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            String generatedFileName = UUID.randomUUID().toString().replace("-","");
            generatedFileName = generatedFileName+"."+fileExtension;
            Path destnationFilePath = this.storageFolder.resolve(
                    Paths.get(generatedFileName)
            ).normalize().toAbsolutePath();
            if(!destnationFilePath.getParent().equals(this.storageFolder.toAbsolutePath())){
                throw new RuntimeException("Cannot store file outside directory.");
            }
            try(InputStream inputStream = file.getInputStream()){
                Files.copy(inputStream, destnationFilePath, StandardCopyOption.REPLACE_EXISTING);
            }


            return generatedFileName;
        }
        catch (IOException e){
            throw new RuntimeException("Failed to store file.", e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try{
            //list all files in storageFolder
            return Files.walk(this.storageFolder, 1)
                    .filter(path -> !path.equals(this.storageFolder) && !path.toString().contains("._"))
                    .map(this.storageFolder::relativize);
        }
        catch (IOException e){
            throw new RuntimeException("failed to load stored files", e);
        }
    }

    @Override
    public byte[] readFileContent(String fileName) {
       try{
        Path file = storageFolder.resolve(fileName);
           Resource resource = new UrlResource(file.toUri());
           if(resource.exists() || resource.isReadable()){
               byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
                       return bytes;
           }
           else {
               throw new RuntimeException(
                       "Could not read file "+fileName
               );
           }
       }catch(IOException e){
           throw new RuntimeException(
                   "Could not read file "+fileName,e
           );
       }
    }

    @Override
    public void deleteAllFiles() {

    }
}
