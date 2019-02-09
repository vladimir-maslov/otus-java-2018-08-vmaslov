package ru.otus.l15.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface THandler<T> {
    T handle(ResultSet resultSet) throws SQLException;
}
