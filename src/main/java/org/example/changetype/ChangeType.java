package org.example.changetype;

import java.util.List;

public interface ChangeType<T> {

    //These methods are just for testing purposes
    String getProperty();

    T getPrevious();

    T getCurrent();

    List<?> getAdded();

    List<?> getRemoved();
}
