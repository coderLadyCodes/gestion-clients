package com.samia.gestion.clients.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "cares")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Care {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "program_id", nullable = true)
    private Program program;

//    @Column(name = "care_price" , columnDefinition = "decimal")
//    private double carePrice;
    @Column(name = "care_price", columnDefinition = "DECIMAL(10,2)")
    private BigDecimal carePrice;  // Use BigDecimal instead of double

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "duration_weeks")
    private int durationWeeks;


    @ElementCollection
    @CollectionTable(name = "care_time_slots", joinColumns = @JoinColumn(name = "care_id"))
    @Column(name = "time_slot")
    private List<String> timeSlot;


    @ElementCollection
    @CollectionTable(name = "care_days_of_week", joinColumns = @JoinColumn(name = "care_id"))
    @Column(name = "days_of_week")
    @Enumerated(EnumType.STRING)
    private List<DayOfWeek> daysOfWeek;

    @Column(name = "created")
    private LocalDate created;

    @Column(name = "modified")
    private LocalDate modified;

    public Care() {}

    public Care(Long clientId, Long userId, Product product, Program program, BigDecimal carePrice, int quantity, int durationWeeks, List<String> timeSlot, List<DayOfWeek> daysOfWeek, LocalDate created, LocalDate modified) {
        this.clientId = clientId;
        this.userId = userId;
        this.product = product;
        this.program = program;
        this.carePrice = carePrice;
        this.quantity = quantity;
        this.durationWeeks = durationWeeks;
        this.timeSlot = timeSlot;
        this.daysOfWeek = daysOfWeek;
        this.created = created;
        this.modified = modified;
    }

    public Care(Long id, Long clientId, Long userId, Product product, Program program, BigDecimal carePrice, int quantity, int durationWeeks, List<String> timeSlot, List<DayOfWeek> daysOfWeek, LocalDate created, LocalDate modified) {
        this.id = id;
        this.clientId = clientId;
        this.userId = userId;
        this.product = product;
        this.program = program;
        this.carePrice = carePrice;
        this.quantity = quantity;
        this.durationWeeks = durationWeeks;
        this.timeSlot = timeSlot;
        this.daysOfWeek = daysOfWeek;
        this.created = created;
        this.modified = modified;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public BigDecimal getCarePrice() {
        return carePrice;
    }

    public void setCarePrice(BigDecimal carePrice) {
        this.carePrice = carePrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getDurationWeeks() {
        return durationWeeks;
    }

    public void setDurationWeeks(int durationWeeks) {
        this.durationWeeks = durationWeeks;
    }

    public List<String> getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(List<String> timeSlot) {
        this.timeSlot = timeSlot;
    }

    public List<DayOfWeek> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(List<DayOfWeek> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public LocalDate getModified() {
        return modified;
    }

    public void setModified(LocalDate modified) {
        this.modified = modified;
    }

    @Override
    public String toString() {
        return "Care{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", userId=" + userId +
                ", product=" + product +
                ", program=" + program +
                ", carePrice=" + carePrice +
                ", quantity=" + quantity +
                ", durationWeeks=" + durationWeeks +
                ", timeSlot=" + timeSlot +
                ", daysOfWeek=" + daysOfWeek +
                ", created=" + created +
                ", modified=" + modified +
                '}';
    }
}
