package ru.otus.l11.db;

import ru.otus.l11.dataset.DataSet;
import ru.otus.l11.dataset.UserDataSet;

import java.util.List;

public interface DBService extends AutoCloseable {
    String getLocalStatus();
    void save(UserDataSet dataSet);
    UserDataSet read(long id);
    UserDataSet readByName(String name);
    List<UserDataSet> readAll();
    void shutdown();
}