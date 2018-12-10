package ru.otus.l12;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.l12.dataset.AddressDataSet;
import ru.otus.l12.dataset.PhoneDataSet;
import ru.otus.l12.dataset.UserDataSet;
import ru.otus.l12.db.DBService;
import ru.otus.l12.db.DBServiceHibernateImpl;
import ru.otus.l12.servlets.AdminServlet;
import ru.otus.l12.servlets.LoginServlet;
import ru.otus.l12.servlets.TemplateProcessor;
import ru.otus.l12.servlets.UserServlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    private final static int PORT = 8090;
    private static final String PUBLIC_HTML = "public_html";

    public static void main(String[] args) throws Exception {
        DBService dbService = initDB();
        initWebServer(dbService);
    }

    private static DBService initDB(){
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

    public static void initWebServer(DBService dbService) throws Exception {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(PUBLIC_HTML);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        TemplateProcessor templateProcessor = new TemplateProcessor();

        context.addServlet(new ServletHolder(new LoginServlet(templateProcessor, "anonymous")), "/login");
        context.addServlet(AdminServlet.class, "/admin");
        context.addServlet(new ServletHolder(new UserServlet(templateProcessor, dbService)), "/user");

        Server server = new Server(PORT);
        server.setHandler(new HandlerList(resourceHandler, context));

        server.start();
        server.join();

        dbService.shutdown();
    }
}
