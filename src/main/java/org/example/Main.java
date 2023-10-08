package org.example;

import org.example.changetype.ChangeType;
import org.example.diff.DiffTool;
import org.example.sample.Car;
import org.example.sample.Engine;
import org.example.sample.Wheel;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        DiffTool diffTool = new DiffTool();

        Wheel wheelFLPrev = new Wheel("fl", "konig", 15.0);
        Wheel wheelFRPrev = new Wheel("fr", "konig", 15.0);
        Wheel wheelRLPrev = new Wheel("rl", "konig", 15.0);
        Wheel wheelRRPrev = new Wheel("rr", "konig", 15.0);

        Engine enginePrev = new Engine("ICE", 120);

        List<String> fixesPrev = List.of("Oil change", "Air filter replacement");

        Car carPrevious = new Car("Toyota", "Tacoma", enginePrev, List.of(wheelFLPrev, wheelFRPrev, wheelRLPrev, wheelRRPrev), fixesPrev);

        Wheel wheelFLCurr = new Wheel("fl", "BBS", 17.0);
        Wheel wheelFRCurr = new Wheel("fr", "BBS", 17.0);
        Wheel wheelRLCurr = new Wheel("rl", "BBS", 17.0);
        Wheel wheelRRCurr = new Wheel("rr", "BBS", 17.0);

        Engine engineCurr = new Engine("ICE", 140);

        List<String> fixesCurr = List.of("Air filter replacement", "Cat battery change");

        Car carCurrent = new Car("Seat", "Leon", engineCurr, List.of(wheelFLCurr, wheelFRCurr, wheelRLCurr, wheelRRCurr), fixesCurr);

        try {
            List<ChangeType> diffs = diffTool.diff(carPrevious, carCurrent);
            for (ChangeType diff : diffs) {
                System.out.println(diff);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
