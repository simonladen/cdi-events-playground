package cool.javaee.cdi.events;

import cool.javaee.cdi.events.database.MessageSaver;
import jakarta.enterprise.event.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventViewBeanTest {

    private EventViewBean eventViewBean;

    @Mock
    private Event<String> simpleMessageEvent;

    @Mock
    private Event<String> importantMessageEvent;

    @Mock
    private MessageSaver messageSaver;

    @BeforeEach
    void setUp() throws Exception {
        eventViewBean = new EventViewBean();
        setField("simpleMessageEvent", simpleMessageEvent);
        setField("importantMessageEvent", importantMessageEvent);
        setField("messageSaver", messageSaver);
    }

    private void setField(String name, Object value) throws Exception {
        Field field = EventViewBean.class.getDeclaredField(name);
        field.setAccessible(true);
        field.set(eventViewBean, value);
    }

    @Test
    void sendSimpleMessageFiresUnqualifiedEvent() {
        eventViewBean.sendSimpleMessage("hello");

        verify(simpleMessageEvent).fire("hello");
        verifyNoInteractions(importantMessageEvent, messageSaver);
    }

    @Test
    void sendImportantMessageFiresImportantQualifiedEvent() {
        eventViewBean.sendImportantMessage("urgent");

        verify(importantMessageEvent).fire("urgent");
        verifyNoInteractions(simpleMessageEvent, messageSaver);
    }

    @Test
    @SuppressWarnings("unchecked")
    void sendImportantMessageAlternativelySelectsImportantQualifierAndFires() {
        Event<String> selectedEvent = mock(Event.class);
        when(simpleMessageEvent.select(any(Annotation.class))).thenReturn(selectedEvent);

        eventViewBean.sendImportantMessageAlternatively("urgent-alt");

        verify(simpleMessageEvent).select(any(Annotation.class));
        verify(selectedEvent).fire("urgent-alt");
    }

    @Test
    void sendMessageToTransactionEventDelegatesToMessageSaver() {
        eventViewBean.sendMessageToTransactionEvent("txn-message");

        verify(messageSaver).saveMessageToDatabase("txn-message");
        verifyNoInteractions(simpleMessageEvent, importantMessageEvent);
    }
}
