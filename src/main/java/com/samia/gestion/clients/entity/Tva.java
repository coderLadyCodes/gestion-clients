package com.samia.gestion.clients.entity;

public enum Tva {
    TVA5_5("TVA5,5%"),
    TVA20("TVA20%");

    private final String value;

    Tva(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    // Optionally: Method to get enum from string (for converting from the front end)
    public static Tva fromValue(String value) {
        for (Tva tva : Tva.values()) {
            if (tva.getValue().equals(value)) {
                return tva;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
