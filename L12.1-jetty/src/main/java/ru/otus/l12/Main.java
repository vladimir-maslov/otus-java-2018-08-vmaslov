package ru.otus.l12;

import ru.otus.l12.db.DBService;
import ru.otus.l12.helper.DBHelper;
import ru.otus.l12.server.WebServer;

public class Main {

    public static void main(String[] args) throws Exception {
        DBService dbService = DBHelper.initDB();
        WebServer server = WebServer.create(dbService);

        server.init();
        server.start();
    }

}
