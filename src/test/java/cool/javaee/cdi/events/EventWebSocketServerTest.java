package cool.javaee.cdi.events;

import jakarta.websocket.RemoteEndpoint;
import jakarta.websocket.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventWebSocketServerTest {

    private final EventWebSocketServer server = new EventWebSocketServer();

    @AfterEach
    void clearStaticSessionRegistry() throws Exception {
        sessions().clear();
    }

    @SuppressWarnings("unchecked")
    private Set<Session> sessions() throws Exception {
        Field field = EventWebSocketServer.class.getDeclaredField("sessions");
        field.setAccessible(true);
        return (Set<Session>) field.get(null);
    }

    @Test
    void onOpenRegistersSession() throws Exception {
        Session session = mock(Session.class);
        when(session.getId()).thenReturn("session-1");

        server.onOpen(session);

        assertTrue(sessions().contains(session));
    }

    @Test
    void onCloseRemovesSession() throws Exception {
        Session session = mock(Session.class);
        when(session.getId()).thenReturn("session-1");
        server.onOpen(session);

        server.onClose(session);

        assertFalse(sessions().contains(session));
    }

    @Test
    void onMessageDoesNotThrow() {
        Session session = mock(Session.class);

        assertDoesNotThrow(() -> server.onMessage("ping", session));
    }

    @Test
    void broadcastSendsToOpenSessionsOnly() {
        Session openSession = mock(Session.class);
        Session closedSession = mock(Session.class);
        RemoteEndpoint.Async openRemote = mock(RemoteEndpoint.Async.class);

        when(openSession.isOpen()).thenReturn(true);
        when(openSession.getAsyncRemote()).thenReturn(openRemote);
        when(closedSession.isOpen()).thenReturn(false);

        server.onOpen(openSession);
        server.onOpen(closedSession);

        EventWebSocketServer.broadcast("hello");

        verify(openRemote).sendText("hello");
        verify(closedSession, never()).getAsyncRemote();
    }

    @Test
    void broadcastIgnoresPerSessionSendFailures() {
        Session session = mock(Session.class);
        RemoteEndpoint.Async remote = mock(RemoteEndpoint.Async.class);
        when(session.isOpen()).thenReturn(true);
        when(session.getAsyncRemote()).thenReturn(remote);
        doThrow(new RuntimeException("boom")).when(remote).sendText(anyString());

        server.onOpen(session);

        assertDoesNotThrow(() -> EventWebSocketServer.broadcast("hello"));
    }

    @Test
    void broadcastWithNoSessionsDoesNotThrow() {
        assertDoesNotThrow(() -> EventWebSocketServer.broadcast("no one listening"));
    }
}
