package com.samia.gestion.clients.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "jwt")
public class Jwt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "valeur")
    private String valeur;
    @Column(name = "desactive")
    private boolean desactive;
    @Column(name = "expire")
    private boolean expire;
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE})
    @JoinColumn(name = "user_id")
    private User user;
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private RefreshToken refreshToken;
    public Jwt() {
    }

    public Jwt(String valeur, boolean desactive, boolean expire, User user, RefreshToken refreshToken) {
        this.valeur = valeur;
        this.desactive = desactive;
        this.expire = expire;
        this.user = user;
        this.refreshToken = refreshToken;
    }
    public Jwt(Long id, String valeur, boolean desactive, boolean expire, User user, RefreshToken refreshToken) {
        this.id = id;
        this.valeur = valeur;
        this.desactive = desactive;
        this.expire = expire;
        this.user = user;
        this.refreshToken = refreshToken;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public boolean isDesactive() {
        return desactive;
    }

    public void setDesactive(boolean desactive) {
        this.desactive = desactive;
    }

    public boolean isExpire() {
        return expire;
    }

    public void setExpire(boolean expire) {
        this.expire = expire;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RefreshToken getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public String toString() {
        return "Jwt{" +
                "id=" + id +
                ", valeur='" + valeur + '\'' +
                ", desactive=" + desactive +
                ", expire=" + expire +
                ", user=" + user +
                ", refreshToken=" + refreshToken +
                '}';
    }

    private Jwt(Builder builder){
        this.id = builder.id;
        this.valeur = builder.valeur;
        this.desactive = builder.desactive;
        this.expire = builder.expire;
        this.user = builder.user;
        this.refreshToken = builder.refreshToken;
    }
    public static Builder builder(){
        return new Builder();
    }
    public static class Builder{
        private Long id;
        private String valeur;
        private boolean desactive;
        private boolean expire;
        private User user;
        private RefreshToken refreshToken;

        public Builder id(Long id){
            this.id = id;
            return this;
        }
        public Builder valeur(String valeur){
            this.valeur = valeur;
            return this;
        }
        public Builder desactive(boolean desactive){
            this.desactive = desactive;
            return this;
        }
        public Builder expire(boolean expire){
            this.expire = expire;
            return this;
        }
        public Builder user(User user){
            this.user = user;
            return this;
        }
        public Builder refreshToken(RefreshToken refreshToken){
            this.refreshToken = refreshToken;
            return this;
        }
        public Jwt build(){
            return new Jwt(this);
        }
    }
}
