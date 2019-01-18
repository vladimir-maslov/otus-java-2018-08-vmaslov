package ru.otus.l14.executor;

import ru.otus.l14.dataset.DataSet;
import ru.otus.l14.handler.Handler;
import ru.otus.l14.handler.THandler;

import java.sql.SQLException;

public interface Executor extends AutoCloseable {
    <T extends DataSet> void save (T user);
    <T extends DataSet> T load (long id, Class<T> clazz);

    void execQuery(String query, Handler handler, Object... parameters) throws SQLException;
    <T> T execQuery(String query, THandler<T> handler, Object... parameters) throws SQLException;
    int execUpdate(String update) throws SQLException;
}
