package ru.otus.l12.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import ru.otus.l12.db.DBService;
import ru.otus.l12.servlets.AdminServlet;
import ru.otus.l12.servlets.LoginServlet;
import ru.otus.l12.servlets.TemplateProcessor;
import ru.otus.l12.servlets.UserServlet;

public class WebServer {
    private final static int PORT = 8090;
    private static final String PUBLIC_HTML = "public_html";
    private static final String LOGIN_VALUE = "anonymous";

    private static final String LOGIN_SERVLET_PATH = "/login";
    private static final String ADMIN_SERVLET_PATH = "/admin";
    private static final String USER_SERVLET_PATH = "/user";

    private final DBService dbService;
    private final Server server;

    private WebServer(DBService dbService) {
        this.dbService = dbService;
        server = new Server(PORT);
    }

    public static WebServer create(DBService dbService) {
        return new WebServer(dbService);
    }

    public void init() throws Exception {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(PUBLIC_HTML);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        TemplateProcessor templateProcessor = new TemplateProcessor();

        context.addServlet(new ServletHolder(new LoginServlet(templateProcessor, LOGIN_VALUE)), LOGIN_SERVLET_PATH);
        context.addServlet(AdminServlet.class, ADMIN_SERVLET_PATH);
        context.addServlet(new ServletHolder(new UserServlet(templateProcessor, dbService)), USER_SERVLET_PATH);

        server.setHandler(new HandlerList(resourceHandler, context));
    }

    public void start() throws Exception {
        server.start();
        server.join();

        dbService.shutdown();
    }
}
