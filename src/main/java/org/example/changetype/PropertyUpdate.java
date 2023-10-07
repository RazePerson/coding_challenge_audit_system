package org.example.changetype;


import java.util.List;

public class PropertyUpdate<T> implements ChangeType {

    private String property;

    private T previous;

    private T current;

    public PropertyUpdate(String property, T previous, T current) {
        this.property = property;
        this.previous = previous;
        this.current = current;
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    @Override
    public String getProperty() {
        return property;
    }

    @Override
    public T getPrevious() {
        return previous;
    }

    @Override
    public T getCurrent() {
        return current;
    }

    @Override
    public List<?> getAdded() {
        return null;
    }

    @Override
    public List<?> getRemoved() {
        return null;
    }

    public static class Builder<T> {
        private String property;

        private T previous;

        private T current;

        public Builder<T> property(String property) {
            this.property = property;
            return this;
        }

        public Builder<T> previous(T previous) {
            this.previous = previous;
            return this;
        }

        public Builder<T> current(T current) {
            this.current = current;
            return this;
        }

        public PropertyUpdate<T> build() {
            return new PropertyUpdate<>(this.property, this.previous, this.current);
        }
    }

    @Override
    public String toString() {
        return "PropertyUpdate{" +
                "property='" + property + '\'' +
                ", previous=" + previous +
                ", current=" + current +
                '}';
    }
}
