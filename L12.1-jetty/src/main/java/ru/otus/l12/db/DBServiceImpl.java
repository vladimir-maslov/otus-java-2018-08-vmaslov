package ru.otus.l12.db;

import ru.otus.l12.dataset.DataSet;
import ru.otus.l12.dataset.UserDataSet;
import ru.otus.l12.exception.ORMException;
import ru.otus.l12.executor.Executor;
import ru.otus.l12.executor.ORMExecutor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBServiceImpl implements DBService {

    private final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS %s " +
            "(id BIGINT(20) NOT NULL AUTO_INCREMENT, " +
            "name VARCHAR(255), " +
            "age INT, " +
            "PRIMARY KEY(id))";

    private final static String DROP_TABLE = "DROP TABLE %s";

    private final Connection connection;
    private Map<Class<? extends DataSet>, ClassInternalsCache> classInternalsCaches = new HashMap<>();

    public DBServiceImpl(Connection connection){
        this.connection = connection;
        try {
            prepareDBTables(UserDataSet.class);
            init(UserDataSet.class);
        } catch (ORMException e){
            e.printStackTrace();
        }
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

    public String getLocalStatus() {
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

    public void prepareDBTables(Class clazz) {
        try {
            Executor executor = new ORMExecutor(getConnection());
            executor.execUpdate(String.format(CREATE_TABLE, clazz.getSimpleName()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteDBTables(Class clazz) {
        try {
            Executor executor = new ORMExecutor(getConnection());
            executor.execUpdate(String.format(DROP_TABLE, clazz.getSimpleName()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(UserDataSet user) {
        Executor executor = new ORMExecutor(getConnection(), getClassCache(user.getClass()));
        executor.save(user);
        return;
    }

    @Override
    public UserDataSet read(long id) {
        Executor executor = new ORMExecutor(getConnection(), getClassCache(UserDataSet.class));
        UserDataSet t = executor.load(id, UserDataSet.class);
        return t;
    }

    @Override
    public UserDataSet readByName(String name) {
        throw new UnsupportedOperationException("Not implemented in ORM");
    }

    @Override
    public List<UserDataSet> readAll() {
        throw new UnsupportedOperationException("Not implemented in ORM");
    }

    @Override
    public Long count() {
        throw new UnsupportedOperationException("Not implemented in ORM");
    }

    @Override
    public void shutdown() {
        try {
            deleteDBTables(UserDataSet.class);
            close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected ClassInternalsCache getClassCache(Class clazz) {
        return classInternalsCaches.get(clazz);
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}