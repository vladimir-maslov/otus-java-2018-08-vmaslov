package ru.otus.l16.dbserver.helper;

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
            String result = field.get(object).toString();
            return result;
        } catch (IllegalAccessException e){
            e.printStackTrace();
        }
        return null;
    }

    public static Method getSetterMethod(Class clazz, String columnName, Method[] methods){
        try {
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
}
