package ru.otus.l16.dbserver.db;

import ru.otus.l16.dbserver.cache.CacheEngine;
import ru.otus.l16.dbserver.dataset.AddressDataSet;
import ru.otus.l16.dbserver.dataset.PhoneDataSet;
import ru.otus.l16.dbserver.dataset.UserDataSet;

import java.util.ArrayList;
import java.util.Arrays;

public class DBHelper {
    public static DBService initDB(String address, CacheEngine cache){
        DBService dbService = new DBServiceHibernateImpl(address, cache);
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
