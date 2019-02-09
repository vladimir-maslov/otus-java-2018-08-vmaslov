package ru.otus.l15.db;

import ru.otus.l15.app.MessageSystemContext;
import ru.otus.l15.cache.CacheEngine;
import ru.otus.l15.dataset.AddressDataSet;
import ru.otus.l15.dataset.PhoneDataSet;
import ru.otus.l15.dataset.UserDataSet;

import ru.otus.l15.app.DBService;

import java.util.ArrayList;
import java.util.Arrays;

public class DBHelper {
    public static DBService initDB(MessageSystemContext context, String address, CacheEngine cache){
        DBService dbService = new DBServiceHibernateImpl(context, address, cache);
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
