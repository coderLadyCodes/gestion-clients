package com.samia.gestion.clients.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "programs")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Program {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "program_reference", unique = true)
    private String programReference;

    @JoinColumn(name = "created_date")
    private LocalDate createdDate;

//    @Column(name = "total_program_price")
//    private double totalProgramPrice;
    @Column(name = "total_program_price", columnDefinition = "DECIMAL(10,2)")
    private BigDecimal totalProgramPrice;


    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Care> cares = new ArrayList<>();

    public Program(){}
    public Program(Long id) {
        this.id = id;
    }

    public Program(Long userId, Long clientId, String programReference, LocalDate createdDate, BigDecimal totalProgramPrice, List<Care> cares) {
        this.userId = userId;
        this.clientId = clientId;
        this.programReference = programReference;
        this.createdDate = createdDate;
        this.totalProgramPrice = totalProgramPrice;
        this.cares = cares;
    }

    public Program(Long id, Long userId, Long clientId, String programReference, LocalDate createdDate, BigDecimal totalProgramPrice, List<Care> cares) {
        this.id = id;
        this.userId = userId;
        this.clientId = clientId;
        this.programReference = programReference;
        this.createdDate = createdDate;
        this.totalProgramPrice = totalProgramPrice;
        this.cares = cares;
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

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getProgramReference() {
        return programReference;
    }

    public void setProgramReference(String programReference) {
        this.programReference = programReference;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public BigDecimal getTotalProgramPrice() {
        return totalProgramPrice;
    }

    public void setTotalProgramPrice(BigDecimal totalProgramPrice) {
        this.totalProgramPrice = totalProgramPrice;
    }

    public List<Care> getCares() {
        return cares;
    }

    public void setCares(List<Care> cares) {
        this.cares = cares;
    }

    @Override
    public String toString() {
        return "Program{" +
                "id=" + id +
                ", userId=" + userId +
                ", clientId=" + clientId +
                ", programReference='" + programReference + '\'' +
                ", createdDate=" + createdDate +
                ", totalProgramPrice=" + totalProgramPrice +
                ", cares=" + cares +
                '}';
    }
}
