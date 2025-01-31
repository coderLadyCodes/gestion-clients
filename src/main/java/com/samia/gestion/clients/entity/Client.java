package com.samia.gestion.clients.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clients", indexes = {
        @Index(name = "idx_last_name_first_name", columnList = "last_name, first_name"),
        @Index(name = "idx_zip_code", columnList = "zip_code")
})
public class Client {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "ce champ est obligatoire")
    @Column(name = "first_name")
    private String firstName;

    @NotBlank(message = "ce champ est obligatoire")
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "comments", columnDefinition="LONGTEXT")
    private String comments;

    @Column(name="email", nullable = true)
    private String email;

    @Column(name="mobile_phone")
    private String mobilePhone;

    @Column(name="home_phone")
    private String homePhone;

    @Column(name="birthday")
    private LocalDate birthday;

    @Column(name="city")
    private String city;

    @Column(name="street_name")
    private String streetName;

    @Column(name="zip_code")
    private String zipCode;

    @Column(name = "created")
    private LocalDate created;

    @Column(name = "modified")
    private LocalDate modified;

    @Column(name = "sex")
    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Column(name = "user_id")
    private Long userId;

    @OneToMany( cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private List<Program> programs = new ArrayList<>();

    public Client() {
    }

    public Client(String firstName, String lastName, String comments, String email, String mobilePhone, String homePhone, LocalDate birthday, String city, String streetName, String zipCode, LocalDate created, LocalDate modified, Sex sex, Long userId, List<Program> programs) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.comments = comments;
        this.email = email;
        this.mobilePhone = mobilePhone;
        this.homePhone = homePhone;
        this.birthday = birthday;
        this.city = city;
        this.streetName = streetName;
        this.zipCode = zipCode;
        this.created = created;
        this.modified = modified;
        this.sex = sex;
        this.userId = userId;
        this.programs = programs;
    }

    public Client(Long id, String firstName, String lastName, String comments, String email, String mobilePhone, String homePhone, LocalDate birthday, String city, String streetName, String zipCode, LocalDate created, LocalDate modified, Sex sex, Long userId, List<Program> programs) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.comments = comments;
        this.email = email;
        this.mobilePhone = mobilePhone;
        this.homePhone = homePhone;
        this.birthday = birthday;
        this.city = city;
        this.streetName = streetName;
        this.zipCode = zipCode;
        this.created = created;
        this.modified = modified;
        this.sex = sex;
        this.userId = userId;
        this.programs = programs;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank(message = "ce champ est obligatoire") String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotBlank(message = "ce champ est obligatoire") String firstName) {
        this.firstName = firstName;
    }

    public @NotBlank(message = "ce champ est obligatoire") String getLastName() {
        return lastName;
    }

    public void setLastName(@NotBlank(message = "ce champ est obligatoire") String lastName) {
        this.lastName = lastName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
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

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Program> getPrograms() {
        return programs;
    }

    public void setPrograms(List<Program> programs) {
        this.programs = programs;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", comments='" + comments + '\'' +
                ", email='" + email + '\'' +
                ", mobilePhone='" + mobilePhone + '\'' +
                ", homePhone='" + homePhone + '\'' +
                ", birthday=" + birthday +
                ", city='" + city + '\'' +
                ", streetName='" + streetName + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", created=" + created +
                ", modified=" + modified +
                ", sex=" + sex +
                ", userId=" + userId +
                ", programs=" + programs +
                '}';
    }
}
