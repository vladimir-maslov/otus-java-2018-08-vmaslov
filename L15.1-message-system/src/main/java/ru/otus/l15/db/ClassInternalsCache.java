package ru.otus.l15.db;

import ru.otus.l15.exception.ORMException;
import ru.otus.l15.helper.ReflectionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class ClassInternalsCache {

    private String name = null;
    private Field[] fields;
    private Method[] methods;

    private String queryInsert;
    private String querySelect;

    private final static String SELECT_DATASET_TEMPLATE = "SELECT * from %s WHERE id = ?";
    private final static String INSERT_DATASET_TEMPLATE = "INSERT INTO %s (%s) VALUES (%s)";

    public String getName() {
        return name;
    }

    public Field[] getFields() {
        return fields;
    }

    public Method[] getMethods() {
        return methods;
    }

    public String getQueryInsert() {
        return queryInsert;
    }

    public String getQuerySelect() {
        return querySelect;
    }

    public static ClassInternalsCache createCache(Class clazz) throws ORMException {
        ClassInternalsCache cache = new ClassInternalsCache();

        cache.name = clazz.getSimpleName();

        if (cache.name == null) {
            throw new ORMException("Class " + clazz.getSimpleName() + "doesn't have a name");
        }

        cache.fields = cacheFields(clazz);
        cache.methods = cacheMethods(clazz);

        if (cache.fields == null || cache.fields.length == 0) {
            throw new ORMException("Class " + clazz.getSimpleName() + "doesn't have any fields");
        }

        cache.querySelect = buildQuerySelect(cache);
        cache.queryInsert = buildQueryInsert(cache);

        if (cache.querySelect == null || cache.queryInsert == null) {
            throw new ORMException("Error while building queries for the class " + clazz.getSimpleName());
        }

        return cache;
    }

    private static Field[] cacheFields(Class clazz) {
        return ReflectionHelper.getClassFields(clazz);
    }

    private static Method[] cacheMethods(Class clazz) {
        return clazz.getMethods();
    }

    private static String buildQueryInsert(ClassInternalsCache cache) {
        ArrayList<String> fieldsNames = new ArrayList<>();
        ArrayList<String> fieldsPlaceholders = new ArrayList<>();

        for (Field field : cache.fields) {
            fieldsNames.add(field.getName());
            fieldsPlaceholders.add("?");
        }

        return String.format(
                INSERT_DATASET_TEMPLATE,
                cache.name,
                String.join(",", fieldsNames),
                String.join(",", fieldsPlaceholders));
    }

    private static String buildQuerySelect(ClassInternalsCache cache) {
        return String.format(
                SELECT_DATASET_TEMPLATE,
                cache.name);
    }
}
