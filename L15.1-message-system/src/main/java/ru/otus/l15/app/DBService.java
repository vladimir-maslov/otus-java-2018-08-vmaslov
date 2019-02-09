package ru.otus.l15.app;

import ru.otus.l15.dataset.UserDataSet;
import ru.otus.l15.messageSystem.Addressee;
import ru.otus.l15.messageSystem.MessageSystem;

import java.util.List;

public interface DBService extends AutoCloseable, Addressee {
    String getLocalStatus();
    void save(UserDataSet dataSet);
    UserDataSet read(long id);
    UserDataSet readByName(String name);
    List<UserDataSet> readAll();
    Long count();
    void shutdown();
}