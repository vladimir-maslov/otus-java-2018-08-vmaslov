package ru.otus.l10.db;

import ru.otus.l10.dataset.DataSet;
import ru.otus.l10.exception.ORMException;
import ru.otus.l10.executor.Executor;
import ru.otus.l10.executor.ORMExecutor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DBServiceORM implements DBService {

    private final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS %s " +
            "(id BIGINT(20) NOT NULL AUTO_INCREMENT, " +
            "name VARCHAR(255), " +
            "age INT, " +
            "PRIMARY KEY(id))";

    private final static String DROP_TABLE = "DROP TABLE %s";

    private final Connection connection;
    private Map<Class<? extends DataSet>, ClassInternalsCache> classInternalsCaches = new HashMap<>();

    public DBServiceORM(Connection connection) {
        this.connection = connection;
    }

    protected Connection getConnection() {
        return connection;
    }

    public void init(Class... clazz) throws ORMException {
        for (Class cl : clazz) {
            ClassInternalsCache cache = ClassInternalsCache.createCache(cl);
            if (cache == null) {
                throw new ORMException("Error while analyzing the class " + cl.getSimpleName());
            }
            classInternalsCaches.put(cl, cache);
        }
    }

    @Override
    public String getMetaData() {
        try {
            return "Connected to: " + getConnection().getMetaData().getURL() + "\n" +
                    "DB name: " + getConnection().getMetaData().getDatabaseProductName() + "\n" +
                    "DB version: " + getConnection().getMetaData().getDatabaseProductVersion() + "\n" +
                    "Driver: " + getConnection().getMetaData().getDriverName();
        } catch (SQLException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Override
    public void prepareDBTables(Class clazz) {
        try {
            Executor executor = new ORMExecutor(getConnection());
            executor.execUpdate(String.format(CREATE_TABLE, clazz.getSimpleName()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteDBTables(Class clazz) {
        try {
            Executor executor = new ORMExecutor(getConnection());
            executor.execUpdate(String.format(DROP_TABLE, clazz.getSimpleName()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T extends DataSet> void save(T user) {
        Executor executor = new ORMExecutor(getConnection(), getClassCache(user.getClass()));
        executor.save(user);
        return;
    }

    @Override
    public <T extends DataSet> T load(long id, Class<T> clazz) {
        Executor executor = new ORMExecutor(getConnection(), getClassCache(clazz));
        T t = executor.load(id, clazz);
        return t;
    }

    protected ClassInternalsCache getClassCache(Class clazz) {
        return classInternalsCaches.get(clazz);
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}