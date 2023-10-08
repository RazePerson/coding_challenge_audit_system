## Coding Challenge: Core of Audit System

### 1. Challenge description
- The description of the coding challenge can be found in the [challenge.md](challenge.md) file.

### 2.Solution
- After understanding the coding challenge and taking some notes, I quickly realised that one of the simpler and quicker solutions would be to use recursion
- The part about recursion is relevant, especially in the 2nd example when nested objects are mentioned:
  
    {"property": "subscription.status", "previous": "ACTIVE", "current": "EXPIRED"}

- As a quick note: I am aware that recursion is not the only answer, and it would be reasonable to look for other tactics if there was a bigger, production-grade project in which this tool is needed
- After the main approach, the next step was to establish the tool's main functionality based on the possible inputs mentioned in the file describing the challenge
- The two parameters being the previous and current state of an object, I will refer to them as "previous" and "current" when describing the steps that I handled in the code:
1. both inputs are null: technically that means that they are the same so no diffs have to show up
2. previous is null and current is not: the difference is the whole current object
3. reverse of 2., i.e. the output shows the previous object as different and the current is null
4. when the inputs are of primitive type, e.g. int, float, char, etc.: in this case, we don't have a "property" so I introduced a default name for that and as a result, I show the difference between the two under that property name
5. when the inputs are lists containing primitives: same as step 4, with the difference that here the result is a list update, i.e. the added/removed values are shown
- from this case onward, the inputs are considered complex and are handled field-by-field because they cannot be null or primitives at this point 
6. complex lists: here, as example 4 shows in the challenge description, I handle the fetching of the id property by either the name of the field or the annotation
   - in case the id is not present either having the annotation or the field's name is "id", I throw a custom exception
   - important to note here that here is the first time recursion appears, because after identifying that indeed both previous and current have the same entry in their lists based on the id, the diff handling is called on the items under those ids
7. the last step is identifying that both parameters are non-null and complex objects, so I start recursively handling the diffs field-by-field, i.e. considering the field's values as the main object and restarting the whole process from step 1 by calling the diff method on them


### 3. Testing
- I used TestNG and AssertJ to test with some sample objects that I have created
- Tests can be run either in an IDE, like Intellij or in the terminal with:
```bash
./gradlew build
```
- I handled most use cases on the sample object called __Car__, which is a nested object that contains lists as well
- The Wheel object has the id field marked by the annotation
- To test the custom exception I created another object, __Bike__ and I didn't mark the bike's wheels' id field with anything
- These steps were more for me to see if my solution works, and also made bug-fixing + cleaning up the code easier because I could easily revalidate every scenario after having the tests
- For a simple manual test, I left a sample code in the Main class, which looks like the following:
```Java
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
```

### 4. Duration
The whole process took around 7.5 hours to complete:
- understanding the problem/note-taking: 1 hour
- basic implementation: 4.5 hours
- testing/debugging/bug-fixing & cleanup: 1.5 hours
- documenting: 30 minutes