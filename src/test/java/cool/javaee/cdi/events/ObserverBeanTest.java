package cool.javaee.cdi.events;

import fish.payara.notification.eventbus.EventbusMessage;
import fish.payara.notification.healthcheck.HealthCheckNotificationData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ObserverBeanTest {

    private final ObserverBean observerBean = new ObserverBean();

    @Test
    void observeHandlesRegularEventWithoutHealthCheckData() {
        EventbusMessage event = mock(EventbusMessage.class);
        when(event.getDomain()).thenReturn("domain");
        when(event.getInstance()).thenReturn("instance");
        when(event.getSubject()).thenReturn("subject");
        when(event.getMessage()).thenReturn("message");
        // getData() is left unstubbed -> Mockito returns null, so the
        // "instanceof HealthCheckNotificationData" check safely evaluates to false.

        assertDoesNotThrow(() -> observerBean.observe(event));
    }

    @Test
    void observeHandlesHealthCheckNotificationDataBranch() {
        EventbusMessage event = mock(EventbusMessage.class);
        HealthCheckNotificationData healthCheckData = mock(HealthCheckNotificationData.class);

        when(event.getDomain()).thenReturn("domain");
        when(event.getInstance()).thenReturn("instance");
        when(event.getSubject()).thenReturn("subject");
        when(event.getMessage()).thenReturn("message");
        when(event.getData()).thenReturn(healthCheckData);
        when(healthCheckData.as(HealthCheckNotificationData.class)).thenReturn(healthCheckData);
        when(healthCheckData.getEntries()).thenReturn(Collections.emptyList());

        assertDoesNotThrow(() -> observerBean.observe(event));
    }

    @Test
    void observeSwallowsBroadcastFailuresGracefully() {
        EventbusMessage event = mock(EventbusMessage.class);
        when(event.getSubject()).thenReturn("subject");
        when(event.getMessage()).thenReturn("message");
        when(event.getDomain()).thenReturn("domain");
        when(event.getInstance()).thenReturn("instance");

        // EventWebSocketServer.broadcast already swallows per-session errors internally,
        // so observe() should never throw even with no sessions registered.
        assertDoesNotThrow(() -> observerBean.observe(event));
    }
}
