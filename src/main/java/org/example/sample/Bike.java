package org.example.sample;

import java.util.List;

public class Bike {

    private String type;

    private List<BikeWheel> wheels;

    public Bike(String type, List<BikeWheel> wheels) {
        this.type = type;
        this.wheels = wheels;
    }
}
