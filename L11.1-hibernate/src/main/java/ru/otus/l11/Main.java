package ru.otus.l11;

import ru.otus.l11.dataset.AddressDataSet;
import ru.otus.l11.dataset.PhoneDataSet;
import ru.otus.l11.dataset.UserDataSet;
import ru.otus.l11.db.DBService;
import ru.otus.l11.db.DBServiceHibernateImpl;
import ru.otus.l11.db.DBServiceImpl;
import ru.otus.l11.helper.ConnectionHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        DBService dbService = new DBServiceHibernateImpl();
        System.out.println(dbService.getLocalStatus());

        UserDataSet user = new UserDataSet("Jimmy", 42,
                new AddressDataSet("New Street"),
                new ArrayList<>(Arrays.asList(new PhoneDataSet("100500"))));

        user.addPhone(new PhoneDataSet("100501"));

        dbService.save(user);

        dbService.save(new UserDataSet("Constantin", 32,
                new AddressDataSet("Down Street"),
                new ArrayList<>(Arrays.asList(new PhoneDataSet("100502")))) );

        printUser(dbService.read(1));
        printUser(dbService.readByName("Constantin"));

        List<UserDataSet> users = dbService.readAll();
        for (UserDataSet usr : users){
            printUser(usr);
        }

        dbService.shutdown();
    }

    private static void printUser(UserDataSet user){
        if (user == null){
            System.out.println("User is not found.");
            return;
        }
        System.out.println(user);
    }
}
