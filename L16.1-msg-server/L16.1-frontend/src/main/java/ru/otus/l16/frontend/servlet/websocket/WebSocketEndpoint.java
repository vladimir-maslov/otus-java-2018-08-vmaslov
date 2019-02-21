package ru.otus.l16.frontend.servlet.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import org.springframework.web.socket.server.standard.SpringConfigurator;
import ru.otus.l16.frontend.app.FrontendService;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.logging.Logger;

@ServerEndpoint(value = "/ws", configurator = SpringConfigurator.class)
@Service
public class WebSocketEndpoint
{
    private int sessionId;

    @Autowired
    private FrontendService frontendService;

    private static Logger log = Logger.getLogger("WebSocket");

    public WebSocketEndpoint(){
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @OnMessage
    public void onMessage(String message, Session session){
        try {
            frontendService.handleRequest(sessionId, message);
        } catch (Exception e){
            log.info(e.toString());
        }
    }

    @OnOpen
    public void onOpen(Session session){
        sessionId = frontendService.addSession(session);
    }

    @OnClose
    public void onClose(){
        frontendService.removeSession(sessionId);
    }
}
