package com.samia.gestion.clients.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories", indexes = {
        @Index(name= "idx_name", columnList = "name")
})
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "tva", nullable = true)
    private Tva tva;

    public Category() {}

    public Category(Long userId, String name, Tva tva) {
        this.userId = userId;
        this.name = name;
        this.tva = tva;
    }

    public Category(Long id, Long userId, String name, Tva tva) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.tva = tva;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Tva getTva() {
        return tva;
    }
    public String getTvaValue() {
        return tva != null ? tva.getValue() : "Aucune TVA";
    }


    public void setTva(Tva tva) {
        this.tva = tva;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", tva=" + tva +
                '}';
    }
}
