package ru.otus.l12.helper;

import ru.otus.l12.dataset.AddressDataSet;
import ru.otus.l12.dataset.PhoneDataSet;
import ru.otus.l12.dataset.UserDataSet;

import ru.otus.l12.db.DBService;
import ru.otus.l12.db.DBServiceHibernateImpl;

import java.util.ArrayList;
import java.util.Arrays;

public class DBHelper {
    public static DBService initDB(){
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

        return dbService;
    }
}
