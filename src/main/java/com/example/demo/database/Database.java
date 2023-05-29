package com.example.demo.database;

import com.example.demo.model.Product;
import com.example.demo.respositories.ProductRespository;


import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class Database {
    private static final Logger logger = LoggerFactory.getLogger(Database.class);
    @Bean
    CommandLineRunner initDatabase(ProductRespository productRespository){
     return new CommandLineRunner() {
         @Override
         public void run(String... args) throws Exception {
//             Product productA = new Product("Orange", 2023, 6000.6, "");
//             Product productB = new Product("Fruit", 2024, 5000.7, "");
//             logger.info("insert data: "+productRespository.save(productA));
//             logger.info("insert data: "+productRespository.save(productB));
         }
     };
    }
}
