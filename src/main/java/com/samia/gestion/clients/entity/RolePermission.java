package com.samia.gestion.clients.entity;

public enum RolePermission {
    // USER
    USER_CREATE,
    USER_UPDATE,
    USER_READ,
    USER_DELETE,

    // ADMIN
    ADMIN_CREATE,
    ADMIN_UPDATE,
    ADMIN_READ,
    ADMIN_DELETE;

    private String valeur;
    RolePermission() {
    }
    RolePermission(String valeur) {
        this.valeur = valeur;
    }
    public String getValeur() {
        return valeur;
    }
}
