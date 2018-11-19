package ru.otus.l09;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class JsonObjectWriter {
    public static String toJson(Object object) {
        String result = processObject(object);
        return result;
    }

    private static String processObject(Object object) {
        if (object == null) return null;

        ArrayList<String> fieldsStrings = new ArrayList<>();

        Field fields[] = object.getClass().getDeclaredFields();
        if (fields == null)
            return null;

        if (isJSONArray(object) || isJSONValue(object))
            return getJsonElement(object);

        for (Field field : fields) {
            try {
                field.setAccessible(true);

                int modifiers = field.getModifiers();
                if (Modifier.isTransient(modifiers) || Modifier.isStatic(modifiers))
                    continue;

                Object value = field.get(object);
                if (value == null)
                    continue;

                fieldsStrings.add(StringWrapper.QUOTES.wrap(field.getName())
                        + ":"
                        + getJsonElement(value));

            } catch (IllegalAccessException e){
                e.printStackTrace();
            }
        }

        return StringWrapper.CURLY_BRACKETS.wrap(String.join(",", fieldsStrings));
    }

    private static String getJsonElement(Object object) {

        if (object == null)
            return null;

        if (isPrimitiveType(object)) {
            return object.toString();
        }

        if (object instanceof Character) {
            return StringWrapper.QUOTES.wrap(object.toString());
        }

        if (object instanceof String) {
            return StringWrapper.QUOTES.wrap(object.toString());
        }

        if (isArray(object)) {
            return getArray(object);
        }

        if (isIterable(object)) {
            return getIterable(object);
        }

        if (object instanceof Map) {
            return getMap(object);
        }

        return processObject(object);
    }

    public static boolean isPrimitiveType(Object object) {
        return (object instanceof Integer
                || object instanceof Short
                || object instanceof Long
                || object instanceof Float
                || object instanceof Double
                || object instanceof Byte
                || object instanceof Boolean);
    }

    public static boolean isArray(Object object) {
        return object.getClass().isArray();
    }

    public static boolean isIterable(Object object) {
        return (object instanceof Iterable);
    }

    public static boolean isJSONArray(Object object){
        return isArray(object)
                || isIterable(object)
                || object instanceof Map;
    }

    public static boolean isJSONValue(Object object){
        return isPrimitiveType(object)
                || (object instanceof String)
                || (object instanceof Character);
    }

    private static String getArray(Object object) {
        ArrayList<String> elements = new ArrayList<>();
        int length = Array.getLength(object);
        for (int i = 0; i < length; i++) {
            Object arrayElement = Array.get(object, i);
            elements.add(getJsonElement(arrayElement));
        }

        return StringWrapper.SQUARE_BRACKETS.wrap(String.join(",", elements));
    }

    public static String getIterable(Object iterable) {
        if (iterable == null) {
            return StringWrapper.SQUARE_BRACKETS.wrap("");
        }
        if (iterable instanceof List) {
            return getArray(((List<Object>) iterable).toArray());
        }
        if (iterable instanceof Set) {
            return getArray(((Set<Object>) iterable).toArray());
        }

        return null;
    }

    private static String getMap(Object object) {
        Map<Object, Object> map = (Map) object;
        if (map == null) {
            return StringWrapper.CURLY_BRACKETS.wrap("");
        }

        ArrayList<String> elements = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            elements.add(StringWrapper.QUOTES.wrap(entry.getKey().toString())
                    + ":"
                    + getJsonElement(entry.getValue()));
        }

        return StringWrapper.CURLY_BRACKETS.wrap(String.join(",", elements));
    }
}