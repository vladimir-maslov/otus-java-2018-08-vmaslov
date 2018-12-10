package ru.otus.l12.db;

import ru.otus.l12.dataset.UserDataSet;

import java.util.List;

public interface DBService extends AutoCloseable {
    String getLocalStatus();
    void save(UserDataSet dataSet);
    UserDataSet read(long id);
    UserDataSet readByName(String name);
    List<UserDataSet> readAll();
    Long count();
    void shutdown();
}