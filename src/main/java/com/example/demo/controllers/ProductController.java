package com.example.demo.controllers;

import com.example.demo.model.Product;
import com.example.demo.model.ResponseObject;
import com.example.demo.respositories.ProductRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/*
Connect with mysql using JPA
docker run -d --rm --name mysql-springboot-base ^
-e MYSQL_ROOT_PASSWORD=123456 ^
-e MYSQL_USER=longhd ^
-e MYSQL_PASSWORD=123456 ^
-e MYSQL_DATABASE=test_db ^
-p 3309:3306 ^
--volume mysql-springboot-base-volume:/var/lib/mysql ^
-d mysql:latest

mysql -h localhost -P3309 --protocol=tcp -u longhd -p // lỗi

docker exec -it mysql8-container
mysql -u root -p
123456
 */
@RestController
@RequestMapping(path = "/api/v1/Products")
public class ProductController {
    //DI = Dependency Injection
    @Autowired
    private ProductRespository respository;

    @GetMapping("/getAllProducts")
    List<Product> getAllProducts(){
        //This request is: http://localhost:8080/api/v1/Products/getAllProducts
        return respository.findAll();
    }
    //Get detail Product
    @GetMapping("/{id}")
    // Let 's return an object with: status, message, data
    ResponseEntity<ResponseObject> findById(@PathVariable Long id){
        Optional<Product> foundProduct = respository.findById(id);
        return foundProduct.isPresent()?
            ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Query successfully", foundProduct)
            ):
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("404", "Cannot find product with id = "+id, "")
            );
    }

    // insert new Product with POST method
    // Postman: raw, JSON
    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertProduct(@RequestBody Product newProduct){
        // 2 products must not have the same name
        List<Product> foundProduct = respository.findByProductName(newProduct.getProductName().trim());
        if(foundProduct.size() > 0){
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("501", "Product name already taken", "")
            );
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseObject("201", "Insert Product successfully !", respository.save(newProduct))
        );
    }

    //update, insert = update if found, otherwise insert
    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateProject(@RequestBody Product newProduct, @PathVariable Long id){
            Product updateProduct = respository.findById(id)
                    .map(product -> {
                        product.setProductName(newProduct.getProductName());
                        product.setYearProduct(newProduct.getYearProduct());
                        product.setPrice(newProduct.getPrice() );
                        product.setUrl(newProduct.getUrl());
                        return respository.save(newProduct);
                    }).orElseGet(()-> {
                        newProduct.setId(id);
                        return respository.save(newProduct);
                    });
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("200", "Update Product Successfully", updateProduct)
            );
    }
    // Delete a Product => DELETE method
    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteProduct(@PathVariable Long id){
        boolean exists = respository.existsById(id);
        if(exists){
            respository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("200", "delete Product successfully !", "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("501", "cannot find product to delete!", "")
        );
    }
}
