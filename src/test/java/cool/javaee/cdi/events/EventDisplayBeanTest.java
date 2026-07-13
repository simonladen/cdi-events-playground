package cool.javaee.cdi.events;

import fish.payara.notification.eventbus.EventbusMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Note: EventDisplayBean calls the static EventWebSocketServer.broadcast(...)
 * inside a try/catch as a best-effort side effect. With no open WebSocket
 * sessions registered, broadcast is a safe no-op, so these tests exercise the
 * observer logic (message recording) directly without needing to mock the
 * WebSocket layer.
 */
@ExtendWith(MockitoExtension.class)
class EventDisplayBeanTest {

    private EventDisplayBean bean;

    @Mock
    private EventbusMessage eventbusMessage;

    @BeforeEach
    void setUp() {
        bean = new EventDisplayBean();
    }

    @Test
    void initAddsStartupMessage() {
        bean.init();

        List<String> messages = bean.getMessages();
        assertEquals(1, messages.size());
        assertTrue(messages.get(0).contains("initialized"));
    }

    @Test
    void getMessagesReturnsEmptyListInitially() {
        assertNotNull(bean.getMessages());
        assertTrue(bean.getMessages().isEmpty());
    }

    @Test
    void clearEmptiesMessages() {
        bean.init();
        assertFalse(bean.getMessages().isEmpty());

        bean.clear();

        assertTrue(bean.getMessages().isEmpty());
    }

    @Test
    void onSimpleStringRecordsMessage() {
        bean.onSimpleString("hello");

        assertEquals(1, bean.getMessages().size());
        assertEquals("[Simple] hello", bean.getMessages().get(0));
    }

    @Test
    void onImportantStringRecordsMessage() {
        bean.onImportantString("urgent");

        assertEquals("[Important] urgent", bean.getMessages().get(0));
    }

    @Test
    void onTransactionStringRecordsMessage() {
        bean.onTransactionString("txn");

        assertEquals("[Transaction] txn", bean.getMessages().get(0));
    }

    @Test
    void onInboundEventRecordsFormattedMessage() {
        when(eventbusMessage.getDomain()).thenReturn("domain1");
        when(eventbusMessage.getInstance()).thenReturn("instance1");
        when(eventbusMessage.getSubject()).thenReturn("subject1");
        when(eventbusMessage.getMessage()).thenReturn("message1");

        bean.onInboundEvent(eventbusMessage);

        assertEquals(1, bean.getMessages().size());
        String entry = bean.getMessages().get(0);
        assertTrue(entry.startsWith("[Inbound]"));
        assertTrue(entry.contains("domain=domain1"));
        assertTrue(entry.contains("instance=instance1"));
        assertTrue(entry.contains("subject=subject1"));
        assertTrue(entry.contains("message=message1"));
    }

    @Test
    void onAnyEventRecordsFormattedMessage() {
        when(eventbusMessage.getDomain()).thenReturn("domain2");
        when(eventbusMessage.getInstance()).thenReturn("instance2");
        when(eventbusMessage.getSubject()).thenReturn("subject2");
        when(eventbusMessage.getMessage()).thenReturn("message2");

        bean.onAnyEvent(eventbusMessage);

        String entry = bean.getMessages().get(0);
        assertTrue(entry.startsWith("[AnyEvent]"));
        assertTrue(entry.contains("domain=domain2"));
    }
}
