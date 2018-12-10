package ru.otus.l12.executor;

import ru.otus.l12.dataset.DataSet;
import ru.otus.l12.db.ClassInternalsCache;
import ru.otus.l12.exception.ORMException;
import ru.otus.l12.handler.Handler;
import ru.otus.l12.handler.THandler;
import ru.otus.l12.helper.ReflectionHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;

public class ORMExecutor implements Executor {
    private final static String SELECT_DATASET_TEMPLATE = "SELECT * from %s WHERE id = ?";
    private final static String INSERT_DATASET_TEMPLATE = "INSERT INTO %s (%s) VALUES (%s)";

    private final Connection connection;
    private final ClassInternalsCache cache;

    public ORMExecutor(Connection connection) {
        this.connection = connection;
        this.cache = null;
    }

    public ORMExecutor(Connection connection, ClassInternalsCache cache) {
        this.connection = connection;
        this.cache = cache;
    }

    @Override
    public void execQuery(String query, Handler handler, Object... parameters) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            prepareStatement(statement, parameters);
            statement.execute();
            ResultSet result = statement.getResultSet();
            handler.handle(result);
        }
    }

    @Override
    public <T> T execQuery(String query, THandler<T> handler, Object... parameters) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            prepareStatement(statement, parameters);
            statement.execute();
            ResultSet result = statement.getResultSet();
            return handler.handle(result);
        }
    }

    @Override
    public int execUpdate(String update) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(update);
            return statement.getUpdateCount();
        }
    }

    private void prepareStatement(PreparedStatement statement, Object... parameters) throws SQLException {
        int paramLength = parameters.length;
        for (int i = 1; i <= paramLength; i++) {
            statement.setObject(i, parameters[i - 1]);
        }
    }

    @Override
    public <T extends DataSet> void save(T user) {
        try {
            if (cache == null)
                throw new ORMException("Class is not supported");

            Field[] fields = cache.getFields();

            ArrayList<String> fieldsValues = new ArrayList<>();
            for (Field field : fields) {
                fieldsValues.add(ReflectionHelper.getFieldValue(field, user));
            }

            String query = cache.getQueryInsert();

            execQuery(query,
                    result -> {
                    },
                    fieldsValues.toArray());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return;
    }

    @Override
    public <T extends DataSet> T load(long id, Class<T> clazz) {
        try {
            if (cache == null)
                throw new ORMException("Class is not supported");

            String query = cache.getQuerySelect();

            return (T) execQuery(query,
                    result -> {
                        if (result.next())
                            return getDataSet(result, clazz);
                        else
                            return null;
                    },
                    id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private <T extends DataSet> T getDataSet(ResultSet result, Class<T> clazz) {
        try {
            Constructor<T> ctor = clazz.getDeclaredConstructor();
            Object object = ctor.newInstance();

            ResultSetMetaData resultSetMetaData = result.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                String columnName = resultSetMetaData.getColumnName(i);
                Method setter = ReflectionHelper.getSetterMethod(clazz, columnName, cache.getMethods());
                if (setter != null) {
                    setter.invoke(object, result.getObject(i));
                }
            }

            return (T) object;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
