package ru.otus.l12.handler;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface Handler {
    void handle(ResultSet resultSet) throws SQLException;
}
