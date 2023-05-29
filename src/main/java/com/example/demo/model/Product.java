package com.example.demo.model;

import jakarta.persistence.*;
import java.util.Calendar;
import java.util.Objects;

@Entity
@Table(name="tblProduct")
public class Product {
    @Id // gan khoa chinh cho thuoc tinh ben duoi
    @GeneratedValue(strategy = GenerationType.AUTO) // gan gia tri duy nhat va auto-icrement cho thuoc tinh ben duoi
     private Long id;
    //Validate = constraint
    @Column(nullable = false, unique = true, length = 300) // cột productName
    private String productName;
    private int yearProduct;
    private double price;
    private String url;

    // none-parameter constructor
    public Product() {
    }
    //calculated field = transient, not exist in Mysql - tạo 1 cột không có trong cơ sở dữ liệu ví dụ cột tổng điểm
    @Transient
    private int age;
    public int getAge(){
        return Calendar.getInstance().get(Calendar.YEAR)- yearProduct;
    }
    public Product(String productName, int yearProduct, double price, String url) {
        this.productName = productName;
        this.yearProduct = yearProduct;
        this.price = price;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getYearProduct() {
        return yearProduct;
    }

    public void setYearProduct(int yearProduct) {
        this.yearProduct = yearProduct;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", yearProduct=" + yearProduct +
                ", price=" + price +
                ", url='" + url + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return yearProduct == product.yearProduct && Double.compare(product.price, price) == 0 && age == product.age && Objects.equals(id, product.id) && Objects.equals(productName, product.productName) && Objects.equals(url, product.url);
    }

}
