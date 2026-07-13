package cool.javaee.cdi.events.observers;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class SimpleEventObserverTest {

    private final SimpleEventObserver observer = new SimpleEventObserver();

    @Test
    void initLogsReadyMessage() {
        assertDoesNotThrow(observer::init);
    }

    @Test
    void simplyObserveHandlesIncomingMessage() throws Exception {
        // simplyObserve is private; CDI invokes it via the container at runtime,
        // so a unit test must reach it via reflection.
        Method method = SimpleEventObserver.class.getDeclaredMethod("simplyObserve", String.class);
        method.setAccessible(true);

        assertDoesNotThrow(() -> method.invoke(observer, "hello"));
    }
}
