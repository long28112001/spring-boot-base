package com.example.demo.respositories;

import com.example.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRespository extends JpaRepository<Product, Long> {
    // JPA tự hiểu là hàm tìm theo ProductName
    List<Product> findByProductName(String productName);
}
