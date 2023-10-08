package org.example.changetype;

import java.util.ArrayList;
import java.util.List;

public class ListUpdate implements ChangeType {

    private String property;

    private List<?> added;

    private List<?> removed;

    public ListUpdate(String property, List<?> added, List<?> removed) {
        this.property = property;
        this.added = added;
        this.removed = removed;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String getProperty() {
        return property;
    }

    @Override
    public Object getPrevious() {
        return null;
    }

    @Override
    public Object getCurrent() {
        return null;
    }

    @Override
    public List<?> getAdded() {
        return added;
    }

    @Override
    public List<?> getRemoved() {
        return removed;
    }

    public static class Builder {

        private String property;

        private List<?> added;

        private List<?> removed;

        public Builder property(String property) {
            this.property = property;
            return this;
        }

        public Builder added(List<?> added) {
            this.added = new ArrayList<>(added);
            return this;
        }

        public Builder removed(List<?> removed) {
            this.removed = new ArrayList<>(removed);
            return this;
        }

        public ListUpdate build() {
            return new ListUpdate(this.property, this.added, this.removed);
        }
    }

    @Override
    public String toString() {
        return "ListUpdate{" +
                "property='" + property + '\'' +
                ", added=" + added +
                ", removed=" + removed +
                '}';
    }
}
