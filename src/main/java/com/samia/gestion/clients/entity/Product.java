package com.samia.gestion.clients.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products", indexes = {
        @Index(name = "idx_name", columnList = "name"),
        @Index(name = "idx_ref_product", columnList = "ref_product"),
        @Index(name = "idx_type", columnList = "type")
})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "type")
    private String type;

    @Column(name = "name")
    private String name;

    @Column(name = "ref_product")
    private String refProduct;

    @Column(name = "description", columnDefinition="LONGTEXT")
    private String description;

    @Column(name = "product_price")
    private double productPrice;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true)
    private Category category;

    public Product(){}

    public Product(Long userId, String type, String name, String refProduct, String description, double productPrice, Category category) {
        this.userId = userId;
        this.type = type;
        this.name = name;
        this.refProduct = refProduct;
        this.description = description;
        this.productPrice = productPrice;
        this.category = category;
    }

    public Product(Long id, Long userId, String type, String name, String refProduct, String description, double productPrice, Category category) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.name = name;
        this.refProduct = refProduct;
        this.description = description;
        this.productPrice = productPrice;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRefProduct() {
        return refProduct;
    }

    public void setRefProduct(String refProduct) {
        this.refProduct = refProduct;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", userId=" + userId +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", refProduct='" + refProduct + '\'' +
                ", description='" + description + '\'' +
                ", productPrice=" + productPrice +
                ", category=" + category +
                '}';
    }
}
