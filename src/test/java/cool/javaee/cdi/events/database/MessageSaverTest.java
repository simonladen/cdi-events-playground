package cool.javaee.cdi.events.database;

import cool.javaee.cdi.events.observers.entities.Message;
import jakarta.enterprise.event.Event;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MessageSaverTest {

    private MessageSaver messageSaver;

    @Mock
    private EntityManager entityManager;

    @Mock
    private Event<String> transactionMessageEvent;

    @BeforeEach
    void setUp() throws Exception {
        messageSaver = new MessageSaver();
        setField("em", entityManager);
        setField("transactionMessageEvent", transactionMessageEvent);
    }

    private void setField(String name, Object value) throws Exception {
        Field field = MessageSaver.class.getDeclaredField(name);
        field.setAccessible(true);
        field.set(messageSaver, value);
    }

    @Test
    void saveMessageToDatabaseFiresEventAndPersistsMessage() {
        messageSaver.saveMessageToDatabase("hello world");

        verify(transactionMessageEvent).fire("hello world");

        ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);
        verify(entityManager).persist(captor.capture());
        assertEquals("hello world", captor.getValue().getEntity());
    }
}
