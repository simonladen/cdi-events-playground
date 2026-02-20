package cool.javaee.cdi.events;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/events")
public class EventWebSocketServer {

    private static final Set<Session> sessions = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        System.out.println("[EventWebSocketServer] session opened: " + session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        System.out.println("[EventWebSocketServer] session closed: " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // echo incoming messages if needed
    }

    public static void broadcast(String message) {
        System.out.println("[EventWebSocketServer] broadcasting message to " + sessions.size() + " sessions: " + message);
        for (Session s : sessions) {
            if (s.isOpen()) {
                try {
                    s.getAsyncRemote().sendText(message);
                } catch (Exception e) {
                    // ignore per-session errors
                }
            }
        }
    }
}
