package ru.otus.l10.db;

import ru.otus.l10.dataset.DataSet;

public interface DBService extends AutoCloseable {
    String getMetaData();
    void prepareDBTables(Class clazz);
    void deleteDBTables(Class clazz);
    <T extends DataSet> void save (T user);
    <T extends DataSet> T load (long id, Class<T> clazz);
}