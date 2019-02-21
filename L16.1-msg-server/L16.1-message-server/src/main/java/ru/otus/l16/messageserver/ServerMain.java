package ru.otus.l16.messageserver;

import ru.otus.l16.messageserver.runner.ProcessRunnerImpl;
import ru.otus.l16.messageserver.server.MessageServer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerMain {
    private static final String DBSERVER_START_COMMAND = "java -jar L16.1-db-server/target/L16.1-db-server.jar";

    private static String FRONTEND_JETTY_HOME = "/usr/local/Cellar/jetty/9.4.14.v20181114";
    private static final String FRONTEND_JETTY_BIN = "/bin/jetty";

    private static final String FRONTEND_WAR_PATH_SOURCE = "L16.1-frontend/target/root.war";
    private static final String FRONTEND_WAR_PATH_DEST = "/libexec/webapps/root.war";

    private static final String FRONTEND_WAR_START_ARGS = " start";

    private static String FRONTEND_JETTY_START_COMMAND = FRONTEND_JETTY_HOME +
            FRONTEND_JETTY_BIN +
            FRONTEND_WAR_START_ARGS;

    private static final int CLIENT_START_DELAY_SEC = 2;

    private static final Logger logger = Logger.getLogger(ServerMain.class.getName());

    public static void main(String[] args) throws Exception {
        if (!(args == null || args.length == 0)) {
            FRONTEND_JETTY_HOME = args[0];
            FRONTEND_JETTY_START_COMMAND = FRONTEND_JETTY_HOME +
                    FRONTEND_JETTY_BIN +
                    FRONTEND_WAR_START_ARGS;
        }

        new ServerMain().start();
    }

    private void start() throws Exception {
        deployFrontendWAR();

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        startClient(executorService, FRONTEND_JETTY_START_COMMAND);
        startClient(executorService, DBSERVER_START_COMMAND);

        MessageServer messageServer = new MessageServer();
        messageServer.start();

        executorService.shutdown();
    }

    private void startClient(ScheduledExecutorService executorService, String command) {
        executorService.schedule(() -> {
            try {
                new ProcessRunnerImpl().start(command);
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        }, CLIENT_START_DELAY_SEC, TimeUnit.SECONDS);
    }

    private static void deployFrontendWAR() throws Exception {
        Files.copy(Paths.get(FRONTEND_WAR_PATH_SOURCE),
                Paths.get(FRONTEND_JETTY_HOME + FRONTEND_WAR_PATH_DEST),
                StandardCopyOption.REPLACE_EXISTING);
    }

}
