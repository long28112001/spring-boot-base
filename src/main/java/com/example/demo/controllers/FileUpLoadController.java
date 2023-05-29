package com.example.demo.controllers;

import com.example.demo.model.ResponseObject;
import com.example.demo.services.IStoreageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/api/v1/FileUpLoad")
public class FileUpLoadController {
    //This controller recevie file/image from client
    @Autowired
    private IStoreageService storeageService;
    @PostMapping("")
    public ResponseEntity<ResponseObject> upLoadFile(@RequestParam("file")MultipartFile file){
        try {
            //save file to a folder => use a service
            String generatedFileName = storeageService.storeFile(file);
            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("200", "Upload file successfully", generatedFileName)
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("501", e.getMessage(),"")
            );
        }
    }
    //how to open this file in Web Server ?
    //get image url
    @GetMapping("/file/{fileName:.+}")
    public ResponseEntity<byte[]> readDetailFile(@PathVariable String fileName){
        try{
            byte[] bytes = storeageService.readFileContent(fileName);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
                    .body(bytes);
        }
        catch (Exception e){
            return ResponseEntity.noContent().build();
        }
    }
    //how to load all uploaded files
    @GetMapping("")
    public ResponseEntity<ResponseObject> getUpLoadFiles(){
        try{
            List<String> urls = storeageService.loadAll()
                    .map(path -> {
                        //convert fileName to url(send request "readDetailFile"
                        String urlPath = MvcUriComponentsBuilder.fromMethodName(FileUpLoadController.class,
                                "readDetailFile", path.getFileName().toString()).build().toUri().toString();
                        return urlPath;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new ResponseObject("200", "List files succesfully", urls));
        }catch (Exception e){
            return ResponseEntity.ok(new ResponseObject("failed", "List files failed", new String[]{}));
        }
    }
}
