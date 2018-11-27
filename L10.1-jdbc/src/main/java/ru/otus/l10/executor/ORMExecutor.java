package ru.otus.l10.executor;

import ru.otus.l10.dataset.DataSet;
import ru.otus.l10.handler.Handler;
import ru.otus.l10.handler.THandler;
import ru.otus.l10.helper.ReflectionHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;

public class ORMExecutor implements Executor {
    private final static String SELECT_DATASET_TEMPLATE = "SELECT * from %s WHERE id = ?";
    private final static String INSERT_DATASET_TEMPLATE = "INSERT INTO %s (%s) VALUES (%s)";

    private final Connection connection;

    public ORMExecutor(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void execQuery(String query, Handler handler) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(query);
            ResultSet result = statement.getResultSet();
            handler.handle(result);
        }
    }

    @Override
    public <T> T execQuery(String query, THandler<T> handler, Object parameters) throws SQLException {
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
            String tableName = user.getClass().getSimpleName();
            Field[] fields = ReflectionHelper.getClassFields(user.getClass());

            if (fields.length == 0) {
                return;
            }

            ArrayList<String> fieldsNames = new ArrayList<>();
            ArrayList<String> fieldsValues = new ArrayList<>();

            for (Field field : fields) {
                fieldsNames.add(field.getName());
                fieldsValues.add(ReflectionHelper.getFieldValue(field, user));
            }

            String query = String.format(
                    INSERT_DATASET_TEMPLATE,
                    tableName,
                    String.join(",", fieldsNames),
                    String.join(",", fieldsValues));

            execQuery(query,
                    result -> {
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }

        return;
    }

    @Override
    public <T extends DataSet> T load(long id, Class<T> clazz) {
        try {
            String tableName = clazz.getSimpleName();

            String query = String.format(
                    SELECT_DATASET_TEMPLATE,
                    tableName);

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
                Method setter = ReflectionHelper.getSetterMethod(clazz, columnName);
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
