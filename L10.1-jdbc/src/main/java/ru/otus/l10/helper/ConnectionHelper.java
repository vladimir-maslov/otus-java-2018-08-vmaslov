package ru.otus.l10.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionHelper {

    private static final String DB_USER = "root";
    private static final String DB_PASS = "pass";
    private static final String DB_NAME = "db_example";

    public static Connection getConnection() {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());

            String url = "jdbc:mysql://" +        //db type
                    "localhost:" +                //host name
                    "3306/" +                     //port
                    "" + DB_NAME + "?" +               //db name
                    "user=" + DB_USER + "&" +     //login
                    "password=" + DB_PASS + "&" + //password
                    "useSSL=false&" +             //do not use Secure Sockets Layer
                    "serverTimezone=UTC";

            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
