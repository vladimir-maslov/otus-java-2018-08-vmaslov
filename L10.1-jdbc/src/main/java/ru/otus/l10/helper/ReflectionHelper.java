package ru.otus.l10.helper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class ReflectionHelper {
    public static Field[] getClassFields(Class clazz){
        if (clazz == null) return null;

        Field fields[] = clazz.getDeclaredFields();
        if (fields == null)
            return null;

        List<Field> fieldsResult = new ArrayList<>();

        for (Field field : fields) {
                field.setAccessible(true);

                int modifiers = field.getModifiers();
                if (Modifier.isTransient(modifiers) || Modifier.isStatic(modifiers))
                    continue;

                fieldsResult.add(field);
        }

        return fieldsResult.toArray(new Field[fieldsResult.size()]);
    }

    public static String getFieldValue(Field field, Object object){
        try {
            String result = stringify(field.get(object));
            return result;
        } catch (IllegalAccessException e){
            e.printStackTrace();
        }
        return null;
    }

    public static Method getSetterMethod(Class clazz, String columnName){
        try {
            Method[] methods = clazz.getMethods();
            for (Method method : methods){
                String setterMethodName = "set" +
                        Character.toUpperCase(columnName.charAt(0)) +
                        columnName.substring(1);

                if (method.getName().equals(setterMethodName)){
                    return method;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String stringify(Object object){
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

        return null;
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
}
