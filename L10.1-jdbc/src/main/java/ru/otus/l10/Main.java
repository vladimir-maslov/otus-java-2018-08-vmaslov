package ru.otus.l10;

import ru.otus.l10.dataset.UserDataSet;
import ru.otus.l10.db.DBServiceORM;
import ru.otus.l10.helper.ConnectionHelper;

import java.sql.Connection;
import java.sql.SQLException;


public class Main {
    public static void main(String[] args) {

        try (Connection connection = ConnectionHelper.getConnection()) {
            DBServiceORM dbServiceORM = new DBServiceORM(connection);

            dbServiceORM.init(UserDataSet.class);
            dbServiceORM.prepareDBTables(UserDataSet.class);

            UserDataSet user = new UserDataSet("Jimmy", 42);
            dbServiceORM.save(user);

            UserDataSet user1 = new UserDataSet("Lars", 20);
            dbServiceORM.save(user1);

            UserDataSet dbUser = dbServiceORM.load(1, UserDataSet.class);
            printUser(dbUser);

            dbServiceORM.deleteDBTables(UserDataSet.class);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private static void printUser(UserDataSet user){
        if (user == null){
            System.out.println("User is not found.");
            return;
        }
        System.out.println("User | " +
                user.getId()
                + ":" + user.getName()
                + ":" + user.getAge());
    }
}
