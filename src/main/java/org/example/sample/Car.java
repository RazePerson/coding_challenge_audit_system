package org.example.sample;

import java.util.List;

public class Car {
    private String make;
    private String model;
    private Engine engine;
    private List<Wheel> wheels;

    private List<String> fixes;

    public Car(String make, String model, Engine engine, List<Wheel> wheels, List<String> fixes) {
        this.make = make;
        this.model = model;
        this.engine = engine;
        this.wheels = wheels;
        this.fixes = fixes;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public Engine getEngine() {
        return engine;
    }

    public List<Wheel> getWheels() {
        return wheels;
    }

    public List<String> getFixes() {
        return fixes;
    }

    @Override
    public String toString() {
        return "Car{" +
                "make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", engine=" + engine +
                ", wheels=" + wheels +
                ", fixes=" + fixes +
                '}';
    }
}
