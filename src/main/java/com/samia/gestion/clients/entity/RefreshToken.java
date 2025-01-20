package com.samia.gestion.clients.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "expire")
    private boolean expire;
    @Column(name = "valeur")
    private String valeur;
    @Column(name= "creation")
    private Instant creation;
    @Column(name= "expiration")
    private Instant expiration;

    public RefreshToken() {
    }

    public RefreshToken(boolean expire, String valeur, Instant creation, Instant expiration) {
        this.expire = expire;
        this.valeur = valeur;
        this.creation = creation;
        this.expiration = expiration;
    }

    public RefreshToken(Long id, boolean expire, String valeur, Instant creation, Instant expiration) {
        this.id = id;
        this.expire = expire;
        this.valeur = valeur;
        this.creation = creation;
        this.expiration = expiration;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isExpire() {
        return expire;
    }

    public void setExpire(boolean expire) {
        this.expire = expire;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public Instant getCreation() {
        return creation;
    }

    public void setCreation(Instant creation) {
        this.creation = creation;
    }

    public Instant getExpiration() {
        return expiration;
    }

    public void setExpiration(Instant expiration) {
        this.expiration = expiration;
    }

    @Override
    public String toString() {
        return "RefreshToken{" +
                "id=" + id +
                ", expire=" + expire +
                ", valeur='" + valeur + '\'' +
                ", creation=" + creation +
                ", expiration=" + expiration +
                '}';
    }

    private RefreshToken(Builder builder){
        this.id = builder.id;
        this.expire = builder.expire;
        this.valeur = builder.valeur;
        this.creation = builder.creation;
        this.expiration = builder.expiration;
    }
    public static Builder builder(){
        return new Builder();
    }
    public static class Builder{
        private Long id;
        private boolean expire;
        private String valeur;
        private Instant creation;
        private Instant expiration;
        public Builder id(Long id){
            this.id = id;
            return this;
        }
        public Builder expire(boolean expire){
            this.expire = expire;
            return this;
        }
        public Builder valeur(String valeur){
            this.valeur = valeur;
            return this;
        }
        public Builder creation(Instant creation){
            this.creation = creation;
            return this;
        }
        public Builder expiration(Instant expiration){
            this.expiration = expiration;
            return this;
        }
        public RefreshToken build(){
            return new RefreshToken(this);
        }
    }
}
