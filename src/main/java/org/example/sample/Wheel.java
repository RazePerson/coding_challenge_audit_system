package org.example.sample;

import org.example.diff.AuditKey;

public class Wheel {

    @AuditKey
    private String position;

    private String brand;
    private double sizeInches;

    public Wheel(String position, String brand, double sizeInches) {
        this.position = position;
        this.brand = brand;
        this.sizeInches = sizeInches;
    }
}

