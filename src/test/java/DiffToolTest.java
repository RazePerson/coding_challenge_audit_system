import org.example.changetype.ChangeType;
import org.example.diff.DiffTool;
import org.example.sample.Car;
import org.example.sample.Engine;
import org.example.sample.Wheel;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class DiffToolTest {

    private static final String DEFAULT_PATH = "DEFAULT_PROPERTY_NAME";

    private Car carPrevious;

    private Car carCurrent;

    private Car carPreviousWithNullEngine;

    private Car carCurrentWithNullFixes;

    private Car carPrevWithNullFixes;

    private Car carPrevWithNullWheel;

    @BeforeClass
    public void setUp() {
        Wheel wheelFLPrev = new Wheel("fl", "konig", 15.0);
        Wheel wheelFRPrev = new Wheel("fr", "konig", 15.0);
        Wheel wheelRLPrev = new Wheel("rl", "konig", 15.0);
        Wheel wheelRRPrev = new Wheel("rr", "konig", 15.0);

        Engine enginePrev = new Engine("ICE", 120);

        List<String> fixesPrev = List.of("Oil change", "Air filter replacement");

        carPrevious = new Car("Toyota", "Tacoma", enginePrev, List.of(wheelFLPrev, wheelFRPrev, wheelRLPrev, wheelRRPrev), fixesPrev);

        Wheel wheelFLCurr = new Wheel("fl", "BBS", 17.0);
        Wheel wheelFRCurr = new Wheel("fr", "BBS", 17.0);
        Wheel wheelRLCurr = new Wheel("rl", "BBS", 17.0);
        Wheel wheelRRCurr = new Wheel("rr", "BBS", 17.0);

        Engine engineCurr = new Engine("ICE", 140);

        List<String> fixesCurr = List.of("Air filter replacement", "Cat battery change");

        carCurrent = new Car("Seat", "Leon", engineCurr, List.of(wheelFLCurr, wheelFRCurr, wheelRLCurr, wheelRRCurr), fixesCurr);

        carPreviousWithNullEngine = new Car("Toyota", "Tacoma", null, List.of(wheelFLPrev, wheelFRPrev, wheelRLPrev, wheelRRPrev), fixesPrev);

        carCurrentWithNullFixes = new Car("Seat", "Leon", engineCurr, List.of(wheelFLCurr, wheelFRCurr, wheelRLCurr, wheelRRCurr), null);

        carPrevWithNullFixes = new Car("Toyota", "Tacoma", enginePrev, List.of(wheelFLPrev, wheelFRPrev, wheelRLPrev, wheelRRPrev), null);

        Wheel incorrectWheel = new Wheel("fl", null, 17.0);
        carPrevWithNullWheel = new Car("Toyota", "Tacoma", enginePrev, List.of(incorrectWheel, wheelFRPrev, wheelRLPrev, wheelRRPrev), fixesPrev);
    }

    @Test
    public void diffToolTest() throws Exception {
        DiffTool diffTool = new DiffTool();
        List<ChangeType> diffs = diffTool.diff(carPrevious, carCurrent);
        assertThat(diffs)
                .filteredOn(changeType -> changeType.getProperty().equals("make"))
                .filteredOn(changeType -> changeType.getPrevious().equals("Toyota") && changeType.getCurrent().equals("Seat"))
                .hasSize(1);
        assertThat(diffs)
                .filteredOn(changeType -> changeType.getProperty().equals("model"))
                .filteredOn(changeType -> changeType.getPrevious().equals("Tacoma") && changeType.getCurrent().equals("Leon"))
                .hasSize(1);
        assertThat(diffs)
                .filteredOn(changeType -> changeType.getProperty().equals("engine.horsepower"))
                .filteredOn(changeType -> changeType.getPrevious().equals(120) && changeType.getCurrent().equals(140))
                .hasSize(1);
        assertThat(diffs)
                .filteredOn(changeType -> changeType.getProperty().equals("wheels[fl].brand"))
                .filteredOn(changeType -> changeType.getPrevious().equals("konig") && changeType.getCurrent().equals("BBS"))
                .hasSize(1);
        assertThat(diffs)
                .filteredOn(changeType -> changeType.getProperty().equals("wheels[fr].sizeInches"))
                .filteredOn(changeType -> changeType.getPrevious().equals(15.0) && changeType.getCurrent().equals(17.0))
                .hasSize(1);
        assertThat(diffs)
                .filteredOn(changeType -> changeType.getProperty().equals("fixes"))
                .filteredOn(changeType -> changeType.getAdded().contains("Cat battery change")
                        && changeType.getRemoved().contains("Oil change"))
                .hasSize(1);
    }

    @Test
    public void primitivesTest() throws Exception {
        DiffTool diffTool = new DiffTool();
        List<ChangeType> diffs = diffTool.diff("Previous string", "Current string");
        assertThat(diffs)
                .filteredOn(changeType -> changeType.getProperty().equals(DEFAULT_PATH))
                .filteredOn(changeType -> changeType.getPrevious().equals("Previous string") && changeType.getCurrent().equals("Current string"))
                .hasSize(1);
    }

    @Test
    public void prevNullEngineTest() throws Exception {
        DiffTool diffTool = new DiffTool();
        List<ChangeType> diffs = diffTool.diff(carPreviousWithNullEngine, carCurrent);

        assertThat(diffs)
                .filteredOn(changeType -> changeType.getProperty().equals("engine"))
                .filteredOn(changeType -> changeType.getPrevious() == null && changeType.getCurrent().equals(carCurrent.getEngine()))
                .hasSize(1);

        assertThat(diffs)
                .filteredOn(changeType -> changeType.getProperty().equals("make"))
                .filteredOn(changeType -> changeType.getPrevious().equals("Toyota") && changeType.getCurrent().equals("Seat"))
                .hasSize(1);
        assertThat(diffs)
                .filteredOn(changeType -> changeType.getProperty().equals("model"))
                .filteredOn(changeType -> changeType.getPrevious().equals("Tacoma") && changeType.getCurrent().equals("Leon"))
                .hasSize(1);
        assertThat(diffs)
                .filteredOn(changeType -> changeType.getProperty().equals("wheels[fl].brand"))
                .filteredOn(changeType -> changeType.getPrevious().equals("konig") && changeType.getCurrent().equals("BBS"))
                .hasSize(1);

        assertThat(diffs)
                .filteredOn(changeType -> changeType.getProperty().equals("wheels[fr].sizeInches"))
                .filteredOn(changeType -> changeType.getPrevious().equals(15.0) && changeType.getCurrent().equals(17.0))
                .hasSize(1);
        assertThat(diffs)
                .filteredOn(changeType -> changeType.getProperty().equals("fixes"))
                .filteredOn(changeType -> changeType.getAdded().contains("Cat battery change")
                        && changeType.getRemoved().contains("Oil change"))
                .hasSize(1);
    }

    @Test
    public void currNullFixesTest() throws Exception {
        DiffTool diffTool = new DiffTool();
        List<ChangeType> diffs = diffTool.diff(carPrevious, carCurrentWithNullFixes);
        assertThat(diffs)
                .filteredOn(changeType -> changeType.getProperty().equals("fixes"))
                .filteredOn(changeType -> changeType.getPrevious().equals(carPrevious.getFixes()) && changeType.getCurrent() == null)
                .hasSize(1);

        assertThat(diffs)
                .filteredOn(changeType -> changeType.getProperty().equals("make"))
                .filteredOn(changeType -> changeType.getPrevious().equals("Toyota") && changeType.getCurrent().equals("Seat"))
                .hasSize(1);
        assertThat(diffs)
                .filteredOn(changeType -> changeType.getProperty().equals("model"))
                .filteredOn(changeType -> changeType.getPrevious().equals("Tacoma") && changeType.getCurrent().equals("Leon"))
                .hasSize(1);
        assertThat(diffs)
                .filteredOn(changeType -> changeType.getProperty().equals("wheels[fl].brand"))
                .filteredOn(changeType -> changeType.getPrevious().equals("konig") && changeType.getCurrent().equals("BBS"))
                .hasSize(1);
        assertThat(diffs)
                .filteredOn(changeType -> changeType.getProperty().equals("wheels[fr].sizeInches"))
                .filteredOn(changeType -> changeType.getPrevious().equals(15.0) && changeType.getCurrent().equals(17.0))
                .hasSize(1);
    }

    @Test
    public void prevNullFixesTest() throws Exception {
        DiffTool diffTool = new DiffTool();
        List<ChangeType> diffs = diffTool.diff(carPrevWithNullFixes, carCurrent);
        assertThat(diffs)
                .filteredOn(changeType -> changeType.getProperty().equals("fixes"))
                .filteredOn(changeType -> changeType.getPrevious() == null && changeType.getCurrent().equals(carCurrent.getFixes()))
                .hasSize(1);

        assertThat(diffs)
                .filteredOn(changeType -> changeType.getProperty().equals("make"))
                .filteredOn(changeType -> changeType.getPrevious().equals("Toyota") && changeType.getCurrent().equals("Seat"))
                .hasSize(1);
        assertThat(diffs)
                .filteredOn(changeType -> changeType.getProperty().equals("model"))
                .filteredOn(changeType -> changeType.getPrevious().equals("Tacoma") && changeType.getCurrent().equals("Leon"))
                .hasSize(1);
        assertThat(diffs)
                .filteredOn(changeType -> changeType.getProperty().equals("wheels[fl].brand"))
                .filteredOn(changeType -> changeType.getPrevious().equals("konig") && changeType.getCurrent().equals("BBS"))
                .hasSize(1);
        assertThat(diffs)
                .filteredOn(changeType -> changeType.getProperty().equals("wheels[fr].sizeInches"))
                .filteredOn(changeType -> changeType.getPrevious().equals(15.0) && changeType.getCurrent().equals(17.0))
                .hasSize(1);
    }

    @Test
    public void incorrectWheelTest() throws Exception {
        DiffTool diffTool = new DiffTool();
        List<ChangeType> diffs = diffTool.diff(carPrevWithNullWheel, carCurrent);

        assertThat(diffs)
                .filteredOn(changeType -> changeType.getProperty().equals("wheels[fl].brand"))
                .filteredOn(changeType -> changeType.getPrevious() == null && changeType.getCurrent().equals("BBS"))
                .hasSize(1);

        assertThat(diffs)
                .filteredOn(changeType -> changeType.getProperty().equals("make"))
                .filteredOn(changeType -> changeType.getPrevious().equals("Toyota") && changeType.getCurrent().equals("Seat"))
                .hasSize(1);
        assertThat(diffs)
                .filteredOn(changeType -> changeType.getProperty().equals("model"))
                .filteredOn(changeType -> changeType.getPrevious().equals("Tacoma") && changeType.getCurrent().equals("Leon"))
                .hasSize(1);
        assertThat(diffs)
                .filteredOn(changeType -> changeType.getProperty().equals("engine.horsepower"))
                .filteredOn(changeType -> changeType.getPrevious().equals(120) && changeType.getCurrent().equals(140))
                .hasSize(1);
        assertThat(diffs)
                .filteredOn(changeType -> changeType.getProperty().equals("wheels[fr].sizeInches"))
                .filteredOn(changeType -> changeType.getPrevious().equals(15.0) && changeType.getCurrent().equals(17.0))
                .hasSize(1);
        assertThat(diffs)
                .filteredOn(changeType -> changeType.getProperty().equals("fixes"))
                .filteredOn(changeType -> changeType.getAdded().contains("Cat battery change")
                        && changeType.getRemoved().contains("Oil change"))
                .hasSize(1);
    }

    @Test
    public void prevNullTest() throws Exception {
        DiffTool diffTool = new DiffTool();
        List<ChangeType> diffs = diffTool.diff(null, carCurrent);
        assertThat(diffs)
                .filteredOn(changeType -> changeType.getProperty().equals(DEFAULT_PATH))
                .filteredOn(changeType -> changeType.getPrevious() == null && changeType.getCurrent().equals(carCurrent))
                .hasSize(1);
    }

    @Test
    public void currNullTest() throws Exception {
        DiffTool diffTool = new DiffTool();
        List<ChangeType> diffs = diffTool.diff(carPrevious, null);
        assertThat(diffs)
                .filteredOn(changeType -> changeType.getProperty().equals(DEFAULT_PATH))
                .filteredOn(changeType -> changeType.getPrevious().equals(carPrevious) && changeType.getCurrent() == null)
                .hasSize(1);
    }

    @Test
    public void bothNullTest() throws Exception {
        DiffTool diffTool = new DiffTool();
        List<ChangeType> diffs = diffTool.diff(null, null);
        assertThat(diffs).isEmpty();
    }

    @Test
    public void randomListTest() throws Exception {
        DiffTool diffTool = new DiffTool();
        List<String> prev = new ArrayList<>();
        prev.add("element1");
        prev.add("element2");

        List<String> curr = new ArrayList<>();
        curr.add("element2");
        curr.add("element3");
        List<ChangeType> diffs = diffTool.diff(prev, curr);
        assertThat(diffs)
                .filteredOn(changeType -> changeType.getProperty().equals(DEFAULT_PATH))
                .filteredOn(changeType -> changeType.getAdded().contains("element3")
                        && changeType.getRemoved().contains("element1"))
                .hasSize(1);
    }

    @Test
    public void doubleTest() throws Exception {
        DiffTool diffTool = new DiffTool();
        List<ChangeType> diffs = diffTool.diff(12.0, 18.0);
        assertThat(diffs)
                .filteredOn(changeType -> changeType.getProperty().equals(DEFAULT_PATH))
                .filteredOn(changeType -> changeType.getPrevious().equals(12.0) && changeType.getCurrent().equals(18.0))
                .hasSize(1);
    }
}
