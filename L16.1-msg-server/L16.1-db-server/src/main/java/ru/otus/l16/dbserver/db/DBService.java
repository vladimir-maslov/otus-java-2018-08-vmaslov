package ru.otus.l16.dbserver.db;

import ru.otus.l16.dbserver.dataset.UserDataSet;
import ru.otus.l16.messageserver.messageSystem.Address;

import java.util.List;

public interface DBService extends AutoCloseable {
    String getLocalStatus();
    void save(UserDataSet dataSet);
    UserDataSet read(long id);
    UserDataSet readByName(String name);
    List<UserDataSet> readAll();
    Long count();
    void shutdown();
    Address getAddress();
}