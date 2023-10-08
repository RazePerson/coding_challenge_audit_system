package org.example.diff;

import org.apache.commons.lang3.ClassUtils;
import org.example.changetype.ChangeType;
import org.example.changetype.ListUpdate;
import org.example.changetype.PropertyUpdate;
import org.example.exceptions.NonexistentIdException;

import java.lang.reflect.Field;
import java.util.*;

public class DiffService {

    private static final String SEPARATOR = ".";

    private static final String DEFAULT_PATH = "DEFAULT_PROPERTY_NAME";

    private static final String ID_PROPERTY_NAME = "id";

    public <T> void handleDiff(String path, T previous, T current, List<ChangeType> changeTypes) throws Exception {
        if (!Objects.equals(previous, current) && sameType(previous, current)) {
            if (isSimpleOrPrimitiveType(previous) || isSimpleOrPrimitiveType(current)) {
                if (!Objects.equals(previous, current)) {
                    changeTypes.add(handlePrimitive(DEFAULT_PATH, previous, current));
                }
            } else if (Collection.class.isAssignableFrom(previous.getClass()) || Collection.class.isAssignableFrom(current.getClass())) {       //handle primitive/simple list
                if (isListPrimitive((List<?>) previous) && isListPrimitive((List<?>) current)) {
                    changeTypes.add(handlePrimitiveList(DEFAULT_PATH, previous, current));
                }
            } else {                                                                                                                            //handle the properties
                Field[] declaredFields = previous.getClass().getDeclaredFields();
                handleByFields(path, previous, current, changeTypes, declaredFields);
            }
        }
    }

    private <T> void handleByFields(String path, T previous, T current, List<ChangeType> changeTypes, Field[] declaredFields) throws Exception {
        for (Field field : declaredFields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            String nextPath = path.isEmpty() ? fieldName : path + SEPARATOR + fieldName;
            Object previousField = field.get(previous);
            Object currentField = field.get(current);

            if (isSimpleOrPrimitiveType(previousField) || isSimpleOrPrimitiveType(currentField)) {              //handle primitive/simple
                if (!Objects.equals(previousField, currentField)) {
                    changeTypes.add(handlePrimitive(nextPath, previousField, currentField));
                }
            } else if (Collection.class.isAssignableFrom(field.getType())) {                                    //handle list
                if (isListPrimitive((List<?>) previousField) && isListPrimitive((List<?>) currentField)) {
                    changeTypes.add(handlePrimitiveList(fieldName, previousField, currentField));
                } else {
                    if (!((List<?>) previousField).isEmpty() && !((List<?>) currentField).isEmpty()) {
                        handleComplexList(nextPath, previousField, currentField, changeTypes);
                    }
                }
            } else {
                handleDiff(nextPath, previousField, currentField, changeTypes);                                 //handle rest (and nested)
            }
        }
    }

    private <T> boolean sameType(T previous, T current) {
        if (previous == null || current == null) {
            return true;
        }
        return Objects.equals(previous.getClass(), current.getClass());
    }

    private boolean isListPrimitive(List<?> field) {
        return field == null || (!field.isEmpty() && isSimpleOrPrimitiveType(field.get(0)));
    }

    private <T> PropertyUpdate handlePrimitive(String path, T previous, T current) {
        return PropertyUpdate.builder()
                .property(path)
                .previous(previous)
                .current(current)
                .build();
    }

    private <T> ListUpdate handlePrimitiveList(String fieldName, T previous, T current) {
        if (previous == null) {
            return ListUpdate.builder()
                    .property(fieldName)
                    .added((List<?>) current)
                    .build();
        }
        if (current == null) {
            return ListUpdate.builder()
                    .property(fieldName)
                    .removed((List<?>) previous)
                    .build();
        }
        List<T> removed = new ArrayList<>((Collection) previous);
        List<T> added = new ArrayList<>((Collection) current);
        removed.removeAll((Collection<?>) current);
        added.removeAll((Collection<?>) previous);
        return ListUpdate.builder()
                .property(fieldName)
                .removed(removed)
                .added(added)
                .build();
    }

    private <T> void handleComplexList(String path, T previous, T current, List<ChangeType> changeTypes) throws Exception {
        List<?> previousList = (List<?>) previous;
        List<?> currentList = (List<?>) current;
        for (int i = 0; i < previousList.size(); i++) {
            Object prev = previousList.get(i);
            Field idFieldPrev = fetchIdField(prev);
            if (currentList.size() >= i) {
                Object curr = currentList.get(i);
                Field idFieldCurr = fetchIdField(curr);
                if (Objects.equals(idFieldPrev, idFieldCurr)) {
                    String newPath = path + "[" + idFieldPrev.get(prev) + "]";
                    handleDiff(newPath, prev, curr, changeTypes);
                }
            }
        }
    }

    private Field fetchIdField(Object item) throws Exception {
        return Arrays.stream(item.getClass().getDeclaredFields())
                .peek(field -> field.setAccessible(true))
                .filter(field -> Objects.equals(field.getName(), ID_PROPERTY_NAME) || field.isAnnotationPresent(AuditKey.class))
                .findFirst()
                .orElseThrow(() -> new NonexistentIdException("Cannot determine ID field!"));
    }

    private <T> boolean isSimpleOrPrimitiveType(T field) {
        return field == null || (field.getClass() == String.class || ClassUtils.isPrimitiveOrWrapper(field.getClass()) || ClassUtils.isAssignable(field.getClass(), Number.class));
    }
}
